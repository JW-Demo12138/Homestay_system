package com.homestay.controller;

import com.homestay.service.SystemConfigService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class SystemConfigController {
    
    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping("/list")
    public Result getAllConfigs() {
        return systemConfigService.getAllConfigs();
    }

    @GetMapping("/{key}")
    public Result getConfigByKey(@PathVariable String key) {
        return systemConfigService.getConfigByKey(key);
    }

    @PutMapping("/{key}")
    public Result updateConfig(@PathVariable String key, @RequestBody Map<String, String> data) {
        String value = data.get("value");
        return systemConfigService.updateConfig(key, value);
    }

    @PutMapping("/batch")
    public Result batchUpdateConfigs(@RequestBody Map<String, String> configs) {
        return systemConfigService.batchUpdateConfigs(configs);
    }
}
