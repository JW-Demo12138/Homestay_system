package com.homestay.controller;

import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 测试地理编码控制器
 * 用于测试百度地图 API 的调用情况
 */
@RestController
@RequestMapping("/api/test")
public class TestGeocodingController {

    @Value("${baidu.map.ak}")
    private String baiduMapAk;

    private final RestTemplate restTemplate;

    public TestGeocodingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 测试百度地图 API 调用
     * @param address 地址字符串
     * @return 百度地图 API 的完整响应
     */
    @GetMapping("/geocoding")
    public Result<?> testGeocoding(@RequestParam String address) {
        try {
            // 构建百度地图地理编码 API URL
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.map.baidu.com/geocoding/v3")
                    .queryParam("address", encodedAddress)
                    .queryParam("output", "json")
                    .queryParam("ak", baiduMapAk);

            URI uri = builder.build().toUri();

            // 发送请求并获取响应
            Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

            return Result.success(response);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("测试失败：" + e.getMessage());
        }
    }
}
