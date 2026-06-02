package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.entity.City;
import com.homestay.mapper.CityMapper;
import com.homestay.service.CityService;
import com.homestay.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    
    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);
    
    @Autowired
    private CityMapper cityMapper;

    @Override
    public Result getHotCities() {
        try {
            logger.info("Getting hot cities...");
            QueryWrapper<City> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_hot", 1);
            queryWrapper.orderByAsc("sort_order");
            List<City> cities = cityMapper.selectList(queryWrapper);
            logger.info("Found {} hot cities", cities != null ? cities.size() : 0);
            return Result.success(cities);
        } catch (Exception e) {
            logger.error("Error getting hot cities", e);
            return Result.error("获取热门城市失败: " + e.getMessage());
        }
    }

    @Override
    public Result getAllCities() {
        try {
            logger.info("Getting all cities...");
            QueryWrapper<City> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("sort_order");
            List<City> cities = cityMapper.selectList(queryWrapper);
            logger.info("Found {} cities", cities != null ? cities.size() : 0);
            return Result.success(cities);
        } catch (Exception e) {
            logger.error("Error getting all cities", e);
            return Result.error("获取所有城市失败: " + e.getMessage());
        }
    }

    @Override
    public Result getCityByName(String name) {
        try {
            logger.info("Getting city by name: {}", name);
            QueryWrapper<City> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            City city = cityMapper.selectOne(queryWrapper);
            if (city == null) {
                return Result.error("城市不存在");
            }
            return Result.success(city);
        } catch (Exception e) {
            logger.error("Error getting city by name: {}", name, e);
            return Result.error("获取城市失败: " + e.getMessage());
        }
    }
}
