package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.entity.City;
import com.homestay.mapper.CityMapper;
import com.homestay.service.CityService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    
    @Autowired
    private CityMapper cityMapper;

    @Override
    public Result getHotCities() {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_hot", 1);
        queryWrapper.orderByAsc("sort_order");
        List<City> cities = cityMapper.selectList(queryWrapper);
        return Result.success(cities);
    }

    @Override
    public Result getAllCities() {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_order");
        List<City> cities = cityMapper.selectList(queryWrapper);
        return Result.success(cities);
    }

    @Override
    public Result getCityByName(String name) {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        City city = cityMapper.selectOne(queryWrapper);
        if (city == null) {
            return Result.error("城市不存在");
        }
        return Result.success(city);
    }
}
