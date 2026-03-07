package com.homestay.service;

import com.homestay.utils.Result;

import java.util.Map;

public interface DashboardService {
    Result getStats();
    Result getRecentOrders();
    Result getRecentHomestays();
}
