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
import java.util.HashMap;
import java.util.Map;

/**
 * 地理编码控制器
 * 负责处理地址转坐标的请求
 * 使用百度地图开放平台的地理编码 API
 */
@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    @Value("${baidu.map.ak}")
    private String baiduMapAk;

    private final RestTemplate restTemplate;

    public GeocodingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 将地址转换为地理坐标
     * @param address 地址字符串
     * @param city 城市名称（可选）
     * @return 包含坐标信息的结果
     */
    @GetMapping("/address-to-coords")
    public Result<?> addressToCoordinates(@RequestParam String address, 
                                         @RequestParam(required = false) String city) {
        try {
            // 构建百度地图地理编码 API URL
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.map.baidu.com/geocoding/v3")
                    .queryParam("address", encodedAddress)
                    .queryParam("output", "json")
                    .queryParam("ak", baiduMapAk);

            // 如果提供了城市参数，添加到请求中
            if (city != null && !city.isEmpty()) {
                builder.queryParam("city", URLEncoder.encode(city, "UTF-8"));
            }

            URI uri = builder.build().toUri();

            // 发送请求并获取响应
            Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

            // 检查响应状态
            if (response == null) {
                // 调用失败时返回默认坐标
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("address", address);
                resultMap.put("longitude", 0.0);
                resultMap.put("latitude", 0.0);
                resultMap.put("coordinateSystem", "BD-09");
                return Result.success(resultMap);
            }

            int status = (int) response.getOrDefault("status", 1);
            if (status != 0) {
                // 地理编码失败时返回默认坐标
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("address", address);
                resultMap.put("longitude", 0.0);
                resultMap.put("latitude", 0.0);
                resultMap.put("coordinateSystem", "BD-09");
                return Result.success(resultMap);
            }

            // 解析坐标信息
            Map<String, Object> result = (Map<String, Object>) response.get("result");
            if (result == null) {
                return Result.error("地理编码失败，无结果");
            }

            Map<String, Double> location = (Map<String, Double>) result.get("location");
            if (location == null) {
                return Result.error("地理编码失败，无坐标信息");
            }

            double lng = location.getOrDefault("lng", 0.0);
            double lat = location.getOrDefault("lat", 0.0);

            // 返回坐标信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("address", address);
            resultMap.put("longitude", lng);
            resultMap.put("latitude", lat);
            resultMap.put("coordinateSystem", "BD-09");
            return Result.success(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("地理编码失败：" + e.getMessage());
        }
    }
}
