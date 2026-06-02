package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.entity.SystemConfig;
import com.homestay.mapper.SystemConfigMapper;
import com.homestay.service.SystemConfigService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {
    
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public Result getAllConfigs() {
        List<SystemConfig> configs = systemConfigMapper.selectList(null);
        Map<String, String> configMap = new HashMap<>();
        for (SystemConfig config : configs) {
            configMap.put(config.getConfigKey(), config.getConfigValue());
        }
        return Result.success("获取配置成功", configMap);
    }

    @Override
    public Result getConfigByKey(String key) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_key", key);
        SystemConfig config = systemConfigMapper.selectOne(queryWrapper);
        if (config != null) {
            return Result.success("获取配置成功", config.getConfigValue());
        }
        return Result.error("配置不存在");
    }

    @Override
    public Result updateConfig(String key, String value) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_key", key);
        SystemConfig config = systemConfigMapper.selectOne(queryWrapper);
        if (config != null) {
            config.setConfigValue(value);
            systemConfigMapper.updateById(config);
            return Result.success("更新配置成功");
        }
        return Result.error("配置不存在");
    }

    @Override
    public Result batchUpdateConfigs(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("config_key", entry.getKey());
            SystemConfig config = systemConfigMapper.selectOne(queryWrapper);
            if (config != null) {
                config.setConfigValue(entry.getValue());
                systemConfigMapper.updateById(config);
            }
        }
        return Result.success("批量更新配置成功");
    }
}
