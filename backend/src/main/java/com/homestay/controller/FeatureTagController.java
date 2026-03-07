package com.homestay.controller;

import com.homestay.service.FeatureTagService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
public class FeatureTagController {
    
    @Autowired
    private FeatureTagService featureTagService;

    @GetMapping("/feature")
    public Result getAllFeatureTags() {
        return featureTagService.getAllFeatureTags();
    }

    @GetMapping("/room-type")
    public Result getAllRoomTypes() {
        return featureTagService.getAllRoomTypes();
    }
}
