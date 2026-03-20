package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 乡村特色体验项目实体类
 * 对应数据库experience表，存储体验项目的基本信息
 */
@TableName("experience")
public class Experience {
    /**
     * 体验项目ID
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 体验名称
     */
    @TableField("name")
    private String name;
    
    /**
     * 体验描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 价格
     */
    @TableField("price")
    private Double price;
    
    /**
     * 时长（分钟）
     */
    @TableField("duration")
    private Integer duration;
    
    /**
     * 可预约时段
     */
    @TableField("available_time")
    private String availableTime;
    
    /**
     * 纬度
     */
    @TableField("latitude")
    private Double latitude;
    
    /**
     * 经度
     */
    @TableField("longitude")
    private Double longitude;
    
    /**
     * 体验地点
     */
    @TableField("location")
    private String location;
    
    /**
     * 关联民宿ID
     */
    @TableField("homestay_id")
    private Long homestayId;
    
    /**
     * 位置范围（米）
     */
    @TableField("radius")
    private Integer radius;
    
    /**
     * 封面图片地址
     */
    @TableField("image_url")
    private String imageUrl;
    
    /**
     * 图片地址（多个，逗号分隔）
     */
    @TableField("images")
    private String images;
    
    /**
     * 体验类型：民俗手工、农事体验、乡村美食制作等
     */
    @TableField("type")
    private String type;
    
    /**
     * 状态：0-下架，1-上架，2-待审核
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 房东ID
     */
    @TableField("owner_id")
    private Long ownerId;
    
    /**
     * 驳回原因
     */
    @TableField("reject_reason")
    private String rejectReason;
    
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
     * 获取体验项目ID
     * 
     * @return Long 体验项目ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置体验项目ID
     * 
     * @param id 体验项目ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取体验名称
     * 
     * @return String 体验名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置体验名称
     * 
     * @param name 体验名称
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取体验描述
     * 
     * @return String 体验描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 设置体验描述
     * 
     * @param description 体验描述
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 获取时长（分钟）
     * 
     * @return Integer 时长
     */
    public Integer getDuration() {
        return duration;
    }
    
    /**
     * 设置时长（分钟）
     * 
     * @param duration 时长
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    /**
     * 获取可预约时段
     * 
     * @return String 可预约时段
     */
    public String getAvailableTime() {
        return availableTime;
    }
    
    /**
     * 设置可预约时段
     * 
     * @param availableTime 可预约时段
     */
    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }
    
    /**
     * 获取纬度
     * 
     * @return Double 纬度
     */
    public Double getLatitude() {
        return latitude;
    }
    
    /**
     * 设置纬度
     * 
     * @param latitude 纬度
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    /**
     * 获取经度
     * 
     * @return Double 经度
     */
    public Double getLongitude() {
        return longitude;
    }
    
    /**
     * 设置经度
     * 
     * @param longitude 经度
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    /**
     * 获取体验地点
     * 
     * @return String 体验地点
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * 设置体验地点
     * 
     * @param location 体验地点
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * 获取封面图片地址
     * 
     * @return String 封面图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }
    
    /**
     * 设置封面图片地址
     * 
     * @param imageUrl 封面图片地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * 获取图片地址（多个，逗号分隔）
     * 
     * @return String 图片地址
     */
    public String getImages() {
        return images;
    }
    
    /**
     * 设置图片地址（多个，逗号分隔）
     * 
     * @param images 图片地址
     */
    public void setImages(String images) {
        this.images = images;
    }
    
    /**
     * 获取体验类型
     * 
     * @return String 体验类型
     */
    public String getType() {
        return type;
    }
    
    /**
     * 设置体验类型
     * 
     * @param type 体验类型
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * 获取状态
     * 
     * @return Integer 状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * 设置状态
     * 
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * 获取房东ID
     * 
     * @return Long 房东ID
     */
    public Long getOwnerId() {
        return ownerId;
    }
    
    /**
     * 设置房东ID
     * 
     * @param ownerId 房东ID
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    /**
     * 获取驳回原因
     * 
     * @return String 驳回原因
     */
    public String getRejectReason() {
        return rejectReason;
    }
    
    /**
     * 设置驳回原因
     * 
     * @param rejectReason 驳回原因
     */
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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
     * 获取关联民宿ID
     * 
     * @return Long 关联民宿ID
     */
    public Long getHomestayId() {
        return homestayId;
    }
    
    /**
     * 设置关联民宿ID
     * 
     * @param homestayId 关联民宿ID
     */
    public void setHomestayId(Long homestayId) {
        this.homestayId = homestayId;
    }
    
    /**
     * 获取位置范围（米）
     * 
     * @return Integer 位置范围
     */
    public Integer getRadius() {
        return radius;
    }
    
    /**
     * 设置位置范围（米）
     * 
     * @param radius 位置范围
     */
    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}