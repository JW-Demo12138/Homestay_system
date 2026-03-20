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
     * 体验项目信息
     * 非数据库字段，用于关联查询
     */
    @TableField(exist = false)
    private Experience experience;
    
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
     * 体验项目ID
     */
    @TableField("experience_id")
    private Long experienceId;
    
    /**
     * 订单类型：HOMESTAY-民宿，EXPERIENCE-体验项目
     */
    @TableField("order_type")
    private String orderType;
    
    /**
     * 体验日期
     */
    @TableField("experience_date")
    private Date experienceDate;
    
    /**
     * 数量
     */
    @TableField("quantity")
    private Integer quantity;
    
    /**
     * 库存锁定过期时间
     */
    @TableField("lock_expire_time")
    private Date lockExpireTime;
    
    /**
     * 体验时间段
     */
    @TableField("experience_time")
    private String experienceTime;
    
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
     * 顾客姓名
     */
    @TableField("guest_name")
    private String guestName;
    
    /**
     * 顾客电话
     */
    @TableField("guest_phone")
    private String guestPhone;
    
    /**
     * 顾客邮箱
     */
    @TableField("guest_email")
    private String guestEmail;
    
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
     * 获取顾客姓名
     * 
     * @return String 顾客姓名
     */
    public String getGuestName() {
        return guestName;
    }
    
    /**
     * 设置顾客姓名
     * 
     * @param guestName 顾客姓名
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    /**
     * 获取顾客电话
     * 
     * @return String 顾客电话
     */
    public String getGuestPhone() {
        return guestPhone;
    }
    
    /**
     * 设置顾客电话
     * 
     * @param guestPhone 顾客电话
     */
    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }
    
    /**
     * 获取顾客邮箱
     * 
     * @return String 顾客邮箱
     */
    public String getGuestEmail() {
        return guestEmail;
    }
    
    /**
     * 设置顾客邮箱
     * 
     * @param guestEmail 顾客邮箱
     */
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
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
    
    /**
     * 获取体验项目信息
     * 
     * @return Experience 体验项目信息
     */
    public Experience getExperience() {
        return experience;
    }
    
    /**
     * 设置体验项目信息
     * 
     * @param experience 体验项目信息
     */
    public void setExperience(Experience experience) {
        this.experience = experience;
    }
    
    /**
     * 获取体验项目ID
     * 
     * @return Long 体验项目ID
     */
    public Long getExperienceId() {
        return experienceId;
    }
    
    /**
     * 设置体验项目ID
     * 
     * @param experienceId 体验项目ID
     */
    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }
    
    /**
     * 获取订单类型
     * 
     * @return String 订单类型
     */
    public String getOrderType() {
        return orderType;
    }
    
    /**
     * 设置订单类型
     * 
     * @param orderType 订单类型
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    
    /**
     * 获取体验日期
     * 
     * @return Date 体验日期
     */
    public Date getExperienceDate() {
        return experienceDate;
    }
    
    /**
     * 设置体验日期
     * 
     * @param experienceDate 体验日期
     */
    public void setExperienceDate(Date experienceDate) {
        this.experienceDate = experienceDate;
    }
    
    /**
     * 获取数量
     * 
     * @return Integer 数量
     */
    public Integer getQuantity() {
        return quantity;
    }
    
    /**
     * 设置数量
     * 
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    /**
     * 获取库存锁定过期时间
     * 
     * @return Date 库存锁定过期时间
     */
    public Date getLockExpireTime() {
        return lockExpireTime;
    }
    
    /**
     * 设置库存锁定过期时间
     * 
     * @param lockExpireTime 库存锁定过期时间
     */
    public void setLockExpireTime(Date lockExpireTime) {
        this.lockExpireTime = lockExpireTime;
    }
    
    /**
     * 获取体验时间段
     * 
     * @return String 体验时间段
     */
    public String getExperienceTime() {
        return experienceTime;
    }
    
    /**
     * 设置体验时间段
     * 
     * @param experienceTime 体验时间段
     */
    public void setExperienceTime(String experienceTime) {
        this.experienceTime = experienceTime;
    }
}