package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 订单实体类
 * 对应数据库order表，存储订单的详细信息
 */
@TableName("`order`")
public class Order {
    /**
     * 订单ID
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     * 下单用户ID，关联user表
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 民宿ID
     * 关联homestay表
     */
    @TableField("homestay_id")
    private Long homestayId;
    
    /**
     * 民宿信息
     * 非数据库字段，用于关联查询
     */
    @TableField(exist = false)
    private com.homestay.entity.Homestay homestay;
    
    /**
     * 入住日期
     */
    @TableField("check_in_date")
    private Date checkInDate;
    
    /**
     * 退房日期
     */
    @TableField("check_out_date")
    private Date checkOutDate;
    
    /**
     * 价格
     */
    private Double price;
    
    /**
     * 订单状态
     * PENDING:待支付, PAID:已支付, CANCELLED:已取消, COMPLETED:已完成
     */
    private String status;
    
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    
    /**
     * 获取订单ID
     * 
     * @return Long 订单ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置订单ID
     * 
     * @param id 订单ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取用户ID
     * 
     * @return Long 用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     * 
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    /**
     * 获取民宿ID
     * 
     * @return Long 民宿ID
     */
    public Long getHomestayId() {
        return homestayId;
    }
    
    /**
     * 设置民宿ID
     * 
     * @param homestayId 民宿ID
     */
    public void setHomestayId(Long homestayId) {
        this.homestayId = homestayId;
    }
    
    /**
     * 获取民宿信息
     * 
     * @return Homestay 民宿信息
     */
    public com.homestay.entity.Homestay getHomestay() {
        return homestay;
    }
    
    /**
     * 设置民宿信息
     * 
     * @param homestay 民宿信息
     */
    public void setHomestay(com.homestay.entity.Homestay homestay) {
        this.homestay = homestay;
    }
    
    /**
     * 获取入住日期
     * 
     * @return Date 入住日期
     */
    public Date getCheckInDate() {
        return checkInDate;
    }
    
    /**
     * 设置入住日期
     * 
     * @param checkInDate 入住日期
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    /**
     * 获取退房日期
     * 
     * @return Date 退房日期
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }
    
    /**
     * 设置退房日期
     * 
     * @param checkOutDate 退房日期
     */
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    /**
     * 获取价格
     * 
     * @return Double 价格
     */
    public Double getPrice() {
        return price;
    }
    
    /**
     * 设置价格
     * 
     * @param price 价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    
    /**
     * 获取订单状态
     * 
     * @return String 订单状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * 设置订单状态
     * 
     * @param status 订单状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * 获取支付时间
     * 
     * @return Date 支付时间
     */
    public Date getPayTime() {
        return payTime;
    }
    
    /**
     * 设置支付时间
     * 
     * @param payTime 支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
    
    /**
     * 获取创建时间
     * 
     * @return Date 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    
    /**
     * 设置创建时间
     * 
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * 获取更新时间
     * 
     * @return Date 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * 设置更新时间
     * 
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}