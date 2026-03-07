package com.homestay.controller;

import com.homestay.service.DashboardService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Result getStats() {
        return dashboardService.getStats();
    }

    @GetMapping("/recent-orders")
    public Result getRecentOrders() {
        return dashboardService.getRecentOrders();
    }

    @GetMapping("/recent-homestays")
    public Result getRecentHomestays() {
        return dashboardService.getRecentHomestays();
    }
}
