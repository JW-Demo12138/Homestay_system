package com.homestay.service;

import com.homestay.entity.SystemConfig;
import com.homestay.utils.Result;

import java.util.Map;

public interface SystemConfigService {
    Result getAllConfigs();
    Result getConfigByKey(String key);
    Result updateConfig(String key, String value);
    Result batchUpdateConfigs(Map<String, String> configs);
}
