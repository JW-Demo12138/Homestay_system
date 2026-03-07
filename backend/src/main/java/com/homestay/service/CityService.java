package com.homestay.service;

import com.homestay.entity.City;
import com.homestay.utils.Result;

import java.util.List;

public interface CityService {
    Result getHotCities();
    Result getAllCities();
    Result getCityByName(String name);
}
