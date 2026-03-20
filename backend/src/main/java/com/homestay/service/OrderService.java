package com.homestay.service;

import com.homestay.entity.Order;
import com.homestay.utils.Result;

/**
 * 订单服务接口
 * 提供订单相关的业务方法
 */
public interface OrderService {

    /**
     * 创建订单
     * <p>
     * 新增订单信息到数据库，默认状态为待支付
     * 
     * @param order 订单信息对象
     * @return Result 创建结果的响应对象
     */
    Result createOrder(Order order);

    /**
     * 获取用户订单
     * <p>
     * 获取当前登录用户的订单列表
     * 
     * @return Result 包含用户订单列表的响应对象
     */
    Result getUserOrders();

    /**
     * 获取房东订单
     * <p>
     * 获取当前登录房东的订单列表
     * 
     * @return Result 包含房东订单列表的响应对象
     */
    Result getLandlordOrders();
    
    /**
     * 获取房东订单（带筛选和分页）
     * <p>
     * 获取当前登录房东的订单列表，支持按状态、民宿名称、日期范围筛选和排序
     * 
     * @param page 页码
     * @param status 订单状态
     * @param homestayName 民宿名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param sortBy 排序字段
     * @param sortOrder 排序顺序
     * @return Result 包含订单列表和总数的响应对象
     */
    Result getLandlordOrdersWithFilter(Integer page, String status, String homestayName, String startDate, String endDate, String sortBy, String sortOrder);

    /**
     * 获取订单详情
     * <p>
     * 根据订单ID查询订单详细信息
     * 
     * @param id 订单ID
     * @return Result 包含订单详情的响应对象
     */
    Result getOrderDetail(Long id);

    /**
     * 更新订单状态
     * <p>
     * 根据订单ID更新订单状态
     * 
     * @param id 订单ID
     * @param status 新的订单状态
     * @return Result 更新结果的响应对象
     */
    Result updateOrderStatus(Long id, String status);

    /**
     * 支付订单
     * <p>
     * 处理订单支付，更新订单状态为已支付
     * 
     * @param id 订单ID
     * @param payMethod 支付方式
     * @return Result 支付结果的响应对象
     */
    Result payOrder(Long id, String payMethod);

    /**
     * 取消订单
     * <p>
     * 取消指定订单，更新订单状态为已取消
     * 
     * @param id 订单ID
     * @return Result 取消结果的响应对象
     */
    Result cancelOrder(Long id);

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
    Result commentOrder(Long id, Integer rating, String content);
    
    /**
     * 锁定库存
     * <p>
     * 锁定预订的库存，设置锁定过期时间
     * 
     * @param order 订单信息对象
     * @return Result 锁定结果的响应对象
     */
    Result lockInventory(Order order);
    
    /**
     * 释放库存
     * <p>
     * 释放锁定的库存，当订单取消或过期时调用
     * 
     * @param orderId 订单ID
     * @return Result 释放结果的响应对象
     */
    Result releaseInventory(Long orderId);
    
    /**
     * 检查库存
     * <p>
     * 检查指定日期的库存是否可用
     * 
     * @param type 类型：HOMESTAY或EXPERIENCE
     * @param itemId 项目ID：民宿ID或体验项目ID
     * @param date 日期
     * @return Result 检查结果的响应对象
     */
    Result checkInventory(String type, Long itemId, String date);
}  
