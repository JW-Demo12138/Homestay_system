package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.dto.OrderDTO;
import com.homestay.entity.Order;
import com.homestay.mapper.OrderMapper;
import com.homestay.service.OrderService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 * 实现OrderService接口，处理订单相关的业务逻辑
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 获取当前登录用户ID
     * <p>
     * 从Spring Security上下文获取当前登录用户的ID
     * 
     * @return Long 当前登录用户的ID
     */
    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * 创建订单
     * <p>
     * 新增订单信息到数据库，默认状态为待支付
     * 
     * @param order 订单信息对象
     * @return Result 创建结果的响应对象
     */
    @Override
    public Result createOrder(Order order) {
        order.setStatus("PENDING");
        if (orderMapper.insert(order) > 0) {
            return Result.success("创建订单成功", order);
        }
        return Result.error("创建订单失败");
    }

    /**
     * 获取用户订单
     * <p>
     * 获取当前登录用户的订单列表
     * 
     * @return Result 包含用户订单列表的响应对象
     */
    @Override
    public Result getUserOrders() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        List<Order> orders = orderMapper.selectOrdersByUserId(currentUserId);
        
        // 转换为前端期望的DTO格式
        List<OrderDTO> orderDTOs = convertToDTOs(orders);
        return Result.success("获取用户订单成功", orderDTOs);
    }

    /**
     * 获取房东订单
     * <p>
     * 获取当前登录房东的订单列表
     * 
     * @return Result 包含房东订单列表的响应对象
     */
    @Override
    public Result getLandlordOrders() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        List<Order> orders = orderMapper.selectOrdersByLandlordId(currentUserId);
        
        List<OrderDTO> orderDTOs = convertToDTOs(orders);
        return Result.success("获取房东订单成功", orderDTOs);
    }
    
    @Override
    public Result getLandlordOrdersWithFilter(Integer page, String status, String homestayName, String startDate, String endDate, String sortBy, String sortOrder) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }

        // 计算分页参数
        int pageSize = 10;
        int start = (page - 1) * pageSize;

        // 获取订单总数
        int total = orderMapper.countOrdersByLandlordIdWithFilter(currentUserId, status, homestayName, startDate, endDate);

        // 获取分页后的订单列表
        List<Order> orders = orderMapper.selectOrdersByLandlordIdWithFilter(currentUserId, status, homestayName, startDate, endDate, sortBy, sortOrder, start, pageSize);

        // 转换为DTO
        List<OrderDTO> orderDTOs = convertToDTOs(orders);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("orders", orderDTOs);
        result.put("total", total);

        return Result.success("获取房东订单成功", result);
    }

    /**
     * 获取订单详情
     * <p>
     * 根据订单ID查询订单详细信息
     * 
     * @param id 订单ID
     * @return Result 包含订单详情的响应对象
     */
    @Override
    public Result getOrderDetail(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        // 转换为前端期望的DTO格式
        OrderDTO orderDTO = convertToDTO(order);
        return Result.success("获取订单详情成功", orderDTO);
    }

    /**
     * 更新订单状态
     * <p>
     * 根据订单ID更新订单状态
     * 
     * @param id 订单ID
     * @param status 新的订单状态
     * @return Result 更新结果的响应对象
     */
    @Override
    public Result updateOrderStatus(Long id, String status) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        order.setStatus(status);
        if (orderMapper.updateById(order) > 0) {
            // 转换为前端期望的DTO格式
            OrderDTO orderDTO = convertToDTO(order);
            return Result.success("更新订单状态成功", orderDTO);
        }
        return Result.error("更新订单状态失败");
    }

    /**
     * 支付订单
     * <p>
     * 处理订单支付，更新订单状态为已支付
     * 
     * @param id 订单ID
     * @param payMethod 支付方式
     * @return Result 支付结果的响应对象
     */
    @Override
    public Result payOrder(Long id, String payMethod) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!"PENDING".equals(order.getStatus())) {
            return Result.error("订单状态错误，无法支付");
        }
        order.setStatus("PAID");
        // 支付方式字段不存在，注释掉
        // order.setPayMethod(payMethod);
        if (orderMapper.updateById(order) > 0) {
            // 转换为前端期望的DTO格式
            OrderDTO orderDTO = convertToDTO(order);
            return Result.success("支付成功", orderDTO);
        }
        return Result.error("支付失败");
    }

    /**
     * 取消订单
     * <p>
     * 取消指定订单，更新订单状态为已取消
     * 
     * @param id 订单ID
     * @return Result 取消结果的响应对象
     */
    @Override
    public Result cancelOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if ("COMPLETED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            return Result.error("订单状态错误，无法取消");
        }
        order.setStatus("CANCELLED");
        if (orderMapper.updateById(order) > 0) {
            // 转换为前端期望的DTO格式
            OrderDTO orderDTO = convertToDTO(order);
            return Result.success("取消订单成功", orderDTO);
        }
        return Result.error("取消订单失败");
    }

    /**
     * 评价订单
     * <p>
     * 对已完成的订单进行评价，包括评分和评价内容
     * 
     * @param id 订单ID
     * @param rating 评分
     * @param content 评价内容
     * @return Result 评价结果的响应对象
     */
    @Override
    public Result commentOrder(Long id, Integer rating, String content) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!"COMPLETED".equals(order.getStatus())) {
            return Result.error("订单状态错误，无法评价");
        }
        // 评分和评价内容字段不存在，注释掉
        // order.setRating(rating);
        // order.setComment(content);
        if (orderMapper.updateById(order) > 0) {
            // 转换为前端期望的DTO格式
            OrderDTO orderDTO = convertToDTO(order);
            return Result.success("评价成功", orderDTO);
        }
        return Result.error("评价失败");
    }

    /**
     * 将Order实体列表转换为OrderDTO列表
     * 
     * @param orders Order实体列表
     * @return OrderDTO列表
     */
    private List<OrderDTO> convertToDTOs(List<Order> orders) {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(convertToDTO(order));
        }
        return orderDTOs;
    }

    /**
     * 将Order实体转换为OrderDTO
     * 
     * @param order Order实体
     * @return OrderDTO
     */
    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        
        if (order.getHomestay() != null) {
            orderDTO.setHomestayImage(order.getHomestay().getImageUrl());
            orderDTO.setHomestayName(order.getHomestay().getName());
            orderDTO.setHomestayAddress(order.getHomestay().getAddress());
        } else {
            orderDTO.setHomestayImage("/uploads/default-homestay.jpg");
            orderDTO.setHomestayName("未知民宿");
            orderDTO.setHomestayAddress("未知地址");
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (order.getCheckInDate() != null) {
            orderDTO.setCheckInDate(sdf.format(order.getCheckInDate()));
        } else {
            orderDTO.setCheckInDate("未知日期");
        }
        
        if (order.getCheckOutDate() != null) {
            orderDTO.setCheckOutDate(sdf.format(order.getCheckOutDate()));
        } else {
            orderDTO.setCheckOutDate("未知日期");
        }
        
        orderDTO.setGuestCount(1);
        orderDTO.setPrice(order.getPrice() != null ? order.getPrice() : 0.0);
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreateTime(order.getCreateTime());
        
        return orderDTO;
    }
}
