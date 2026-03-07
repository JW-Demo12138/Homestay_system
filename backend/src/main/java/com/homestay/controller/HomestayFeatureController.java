package com.homestay.controller;

import com.homestay.service.HomestayFeatureService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feature")
public class HomestayFeatureController {
    
    @Autowired
    private HomestayFeatureService homestayFeatureService;

    @GetMapping("/list")
    public Result getFeatureList(@RequestParam Map<String, Object> params) {
        return homestayFeatureService.getFeatureList(params);
    }

    @PutMapping("/status/{id}")
    public Result updateFeatureStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusData) {
        Integer status = statusData.get("status");
        return homestayFeatureService.updateFeatureStatus(id, status);
    }

    @GetMapping("/topic")
    public Result getFeatureTopic(@RequestParam String tag) {
        return homestayFeatureService.getFeatureTopic(tag);
    }
}
