package com.homestay.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import reactor.util.retry.Retry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.apache.commons.codec.digest.DigestUtils;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

@RestController
@RequestMapping("/api/map")
@Slf4j
public class MapController {

    @Value("${baidu.map.ak}")
    private String ak;
    
    @Value("${baidu.map.timeout:10}")
    private int timeoutSeconds;

    private final WebClient webClient;
    private final CacheManager cacheManager; // 用于手动清理

    public MapController(CacheManager cacheManager) {
        this.cacheManager = Objects.requireNonNull(cacheManager, "CacheManager cannot be null");
        // 配置连接池和超时
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(timeoutSeconds));
                    conn.addHandlerLast(new WriteTimeoutHandler(5));
                });

        this.webClient = WebClient.builder()
                .baseUrl("https://api.map.baidu.com")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    @GetMapping("/route")
    public Mono<ResponseEntity<?>> getRoute(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "driving") String mode,
            @RequestParam(defaultValue = "false") boolean refresh) throws java.io.UnsupportedEncodingException {
        
        // 生成安全缓存Key（MD5防特殊字符）
        String cacheKey = generateCacheKey(origin, destination, mode);
        
        // 强制刷新时清理缓存
        if (refresh) {
            Objects.requireNonNull(cacheManager.getCache("routeCache")).evict(cacheKey);
        }

        // 检查本地缓存（Caffeine一级缓存，比Redis快）
        Cache routeCache = cacheManager.getCache("routeCache");
        final Cache.ValueWrapper cached;
        if (routeCache != null) {
            cached = routeCache.get(cacheKey);
        } else {
            cached = null;
        }
        if (cached != null && !refresh) {
            log.debug("命中缓存: {}", cacheKey);
            return Mono.just(ResponseEntity.ok()
                    .header("X-Cache", "HIT")
                    .body(cached.get()));
        }

        // URL编码参数（关键！）
        String encodedOrigin = URLEncoder.encode(origin, StandardCharsets.UTF_8.name());
        String encodedDest = URLEncoder.encode(destination, StandardCharsets.UTF_8.name());

        String url = String.format("/directionlite/v1/%s?origin=%s&destination=%s&ak=%s",
                mode, encodedOrigin, encodedDest, ak);

        log.info("请求百度路线规划: {} -> {}, 方式: {}", origin, destination, mode);

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isError(), clientResponse -> {
                    // 记录百度API错误日志
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("百度API错误: {}, 响应: {}", clientResponse.statusCode(), errorBody);
                                return Mono.error(new RuntimeException("地图服务暂时不可用"));
                            });
                })
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500))  // 失败重试2次
                        .filter(throwable -> !(throwable instanceof TimeoutException)))
                .flatMap(response -> {
                    // 验证JSON有效性
                    try {
                        JsonNode json = new ObjectMapper().readTree(response);
                        if (json.get("status").asInt() != 0) {
                            String msg = json.get("message").asText("未知错误");
                            log.warn("百度API业务错误: {}", msg);
                            Map<String, Object> errorMap = new java.util.HashMap<>();
                            errorMap.put("error", msg);
                            errorMap.put("baiduStatus", json.get("status").asInt());
                            return Mono.just(ResponseEntity.badRequest()
                                    .body(errorMap));
                        }
                        
                        // 缓存成功结果（Redis + Caffeine多级缓存）
                        if (routeCache != null) {
                            routeCache.put(cacheKey, response);
                        }
                        
                        return Mono.just(ResponseEntity.ok()
                                .header("X-Cache", "MISS")
                                .header("Cache-Control", "max-age=3600")
                                .body(response));
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> {
                    // 降级：返回缓存（即使过期）或友好错误
                    if (cached != null) {
                        log.warn("使用过期缓存降级: {}", e.getMessage());
                        return Mono.just(ResponseEntity.ok()
                                .header("X-Cache", "STALE")
                                .body(cached.get()));
                    }
                    log.error("路线规划失败: {}", e.getMessage());
                    Map<String, Object> errorMap = new java.util.HashMap<>();
                    errorMap.put("error", "地图服务繁忙，请稍后重试");
                    errorMap.put("detail", e.getMessage());
                    return Mono.just(ResponseEntity.status(503)
                            .body(errorMap));
                });
    }

    private String generateCacheKey(String origin, String dest, String mode) {
        // MD5避免特殊字符问题，同时缩短Key长度
        String raw = String.format("%s|%s|%s", origin.trim(), dest.trim(), mode);
        return DigestUtils.md5Hex(raw.getBytes());
    }
}