package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.entity.Homestay;
import com.homestay.entity.Order;
import com.homestay.entity.User;
import com.homestay.mapper.HomestayMapper;
import com.homestay.mapper.OrderMapper;
import com.homestay.mapper.UserMapper;
import com.homestay.service.DashboardService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private HomestayMapper homestayMapper;
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        QueryWrapper<Homestay> homestayQuery = new QueryWrapper<>();
        stats.put("totalHomestays", homestayMapper.selectCount(homestayQuery));
        
        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        stats.put("totalOrders", orderMapper.selectCount(orderQuery));
        
        QueryWrapper<Order> revenueQuery = new QueryWrapper<>();
        revenueQuery.eq("status", "COMPLETED");
        List<Order> completedOrders = orderMapper.selectList(revenueQuery);
        Double totalRevenue = completedOrders.stream()
                .map(Order::getPrice)
                .filter(price -> price != null)
                .reduce(0.0, Double::sum);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue.intValue() : 0);
        
        QueryWrapper<Order> pendingQuery = new QueryWrapper<>();
        pendingQuery.eq("status", "PENDING");
        stats.put("pendingOrders", orderMapper.selectCount(pendingQuery));
        
        return Result.success("获取统计数据成功", stats);
    }

    @Override
    public Result getRecentOrders() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT 5");
        List<Order> orders = orderMapper.selectList(queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("total", orders.size());
        
        return Result.success("获取最近订单成功", result);
    }

    @Override
    public Result getRecentHomestays() {
        QueryWrapper<Homestay> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT 10");
        List<Homestay> homestays = homestayMapper.selectList(queryWrapper);
        return Result.success("获取最近民宿成功", homestays);
    }
}
