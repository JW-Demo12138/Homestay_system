package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestay.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 * 继承BaseMapper，提供订单相关的数据库操作方法
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据房东ID查询订单
     * <p>
     * 查询该房东所有民宿的订单
     * 
     * @param landlordId 房东ID
     * @return List<Order> 订单列表
     */
    List<Order> selectOrdersByLandlordId(@Param("landlordId") Long landlordId);
    
    /**
     * 根据用户ID查询订单（关联民宿信息）
     * <p>
     * 查询该用户的所有订单，并关联民宿信息
     * 
     * @param userId 用户ID
     * @return List<Order> 订单列表
     */
    List<Order> selectOrdersByUserId(@Param("userId") Long userId);
    
    /**
     * 根据房东ID查询订单（带筛选和排序）
     * <p>
     * 查询该房东所有民宿的订单，支持按状态、民宿名称、日期范围筛选和排序
     * 
     * @param landlordId 房东ID
     * @param status 订单状态
     * @param homestayName 民宿名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param sortBy 排序字段
     * @param sortOrder 排序顺序
     * @param start 起始索引
     * @param pageSize 每页大小
     * @return List<Order> 订单列表
     */
    List<Order> selectOrdersByLandlordIdWithFilter(@Param("landlordId") Long landlordId,
                                                  @Param("status") String status,
                                                  @Param("homestayName") String homestayName,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("sortBy") String sortBy,
                                                  @Param("sortOrder") String sortOrder,
                                                  @Param("start") Integer start,
                                                  @Param("pageSize") Integer pageSize);
    
    /**
     * 根据房东ID查询订单总数（带筛选）
     * <p>
     * 查询该房东所有民宿的订单总数，支持按状态、民宿名称、日期范围筛选
     * 
     * @param landlordId 房东ID
     * @param status 订单状态
     * @param homestayName 民宿名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return int 订单总数
     */
    int countOrdersByLandlordIdWithFilter(@Param("landlordId") Long landlordId,
                                         @Param("status") String status,
                                         @Param("homestayName") String homestayName,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate);
                                         
    /**
     * 管理员查询订单列表（关联民宿信息）
     * 
     * @param status 订单状态
     * @param keyword 关键词（顾客姓名、电话、邮箱）
     * @param start 起始索引
     * @param pageSize 每页大小
     * @return List<Order> 订单列表
     */
    List<Order> selectAdminOrders(@Param("status") String status,
                                   @Param("keyword") String keyword,
                                   @Param("start") Integer start,
                                   @Param("pageSize") Integer pageSize);
                                   
    /**
     * 管理员查询订单总数
     * 
     * @param status 订单状态
     * @param keyword 关键词
     * @return int 订单总数
     */
    int countAdminOrders(@Param("status") String status,
                         @Param("keyword") String keyword);
                         
    /**
     * 根据订单ID查询订单详情（关联民宿、体验项目、用户信息）
     * 
     * @param id 订单ID
     * @return Order 订单对象（包含关联信息）
     */
    Order selectOrderByIdWithRelations(@Param("id") Long id);
}