package com.homestay.controller;

import com.homestay.entity.Order;
import com.homestay.service.OrderService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单控制器
 * 处理订单相关的HTTP请求，包括创建订单、获取用户订单、获取房东订单、获取订单详情、更新订单状态、支付订单、取消订单和评价订单等操作
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    /**
     * 订单服务，处理订单相关的业务逻辑
     */
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单接口
     * @param order 订单信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result createOrder(@RequestBody Order order) {
        // 调用订单服务创建订单
        return orderService.createOrder(order);
    }

    /**
     * 获取用户订单列表接口
     * @return 用户订单列表
     */
    @GetMapping("/list")
    public Result getUserOrders() {
        // 调用订单服务获取用户订单列表
        return orderService.getUserOrders();
    }

    /**
     * 获取房东订单列表接口
     * @return 房东订单列表
     */
    @GetMapping("/landlord/list")
    public Result getLandlordOrders() {
        // 调用订单服务获取房东订单列表
        return orderService.getLandlordOrders();
    }
    
    /**
     * 获取房东订单列表（带筛选和分页）
     * @param page 页码
     * @param status 订单状态
     * @param homestayName 民宿名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param sortBy 排序字段
     * @param sortOrder 排序顺序
     * @return 订单列表和总数
     */
    @GetMapping("/landlord/orders")
    public Result getLandlordOrdersWithFilter(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(required = false) String status,
                                             @RequestParam(required = false) String homestayName,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate,
                                             @RequestParam(required = false) String sortBy,
                                             @RequestParam(required = false) String sortOrder) {
        return orderService.getLandlordOrdersWithFilter(page, status, homestayName, startDate, endDate, sortBy, sortOrder);
    }

    /**
     * 获取订单详情接口
     * @param id 订单ID
     * @return 订单详情信息
     */
    @GetMapping("/detail/{id}")
    public Result getOrderDetail(@PathVariable Long id) {
        // 调用订单服务获取订单详情
        return orderService.getOrderDetail(id);
    }

    /**
     * 更新订单状态接口
     * @param id 订单ID
     * @param statusData 状态数据，包含新的订单状态
     * @return 更新结果
     */
    @PutMapping("/update/{id}")
    public Result updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        // 从请求数据中获取订单状态
        String status = statusData.get("status");
        // 调用订单服务更新订单状态
        return orderService.updateOrderStatus(id, status);
    }

    /**
     * 支付订单接口
     * @param id 订单ID
     * @param payData 支付数据，包含支付方式
     * @return 支付结果
     */
    @PostMapping("/pay/{id}")
    public Result payOrder(@PathVariable Long id, @RequestBody Map<String, String> payData) {
        // 从请求数据中获取支付方式
        String payMethod = payData.get("payMethod");
        // 调用订单服务支付订单
        return orderService.payOrder(id, payMethod);
    }

    /**
     * 取消订单接口
     * @param id 订单ID
     * @return 取消结果
     */
    @PostMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id) {
        // 调用订单服务取消订单
        return orderService.cancelOrder(id);
    }

    /**
     * 评价订单接口
     * @param id 订单ID
     * @param commentData 评价数据，包含评分和评价内容
     * @return 评价结果
     */
    @PostMapping("/comment/{id}")
    public Result commentOrder(@PathVariable Long id, @RequestBody Map<String, Object> commentData) {
        // 从请求数据中获取评分和评价内容
        Integer rating = (Integer) commentData.get("rating");
        String content = (String) commentData.get("content");
        // 调用订单服务评价订单
        return orderService.commentOrder(id, rating, content);
    }
    
    /**
     * 锁定库存接口
     * @param order 订单信息
     * @return 锁定结果
     */
    @PostMapping("/lock")
    public Result lockInventory(@RequestBody Order order) {
        return orderService.lockInventory(order);
    }
    
    /**
     * 释放库存接口
     * @param orderId 订单ID
     * @return 释放结果
     */
    @PostMapping("/release/{orderId}")
    public Result releaseInventory(@PathVariable Long orderId) {
        return orderService.releaseInventory(orderId);
    }
    
    /**
     * 检查库存接口
     * @param type 类型：HOMESTAY或EXPERIENCE
     * @param itemId 项目ID：民宿ID或体验项目ID
     * @param date 日期
     * @return 检查结果
     */
    @GetMapping("/check-inventory")
    public Result checkInventory(@RequestParam String type, @RequestParam Long itemId, @RequestParam String date) {
        return orderService.checkInventory(type, itemId, date);
    }
}
