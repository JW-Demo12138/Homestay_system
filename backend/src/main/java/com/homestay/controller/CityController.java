package com.homestay.controller;

import com.homestay.service.CityService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/city")
public class CityController {
    
    @Autowired
    private CityService cityService;

    @GetMapping("/hot")
    public Result getHotCities() {
        return cityService.getHotCities();
    }

    @GetMapping("/all")
    public Result getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/search")
    public Result getCityByName(@RequestParam String name) {
        return cityService.getCityByName(name);
    }
}




