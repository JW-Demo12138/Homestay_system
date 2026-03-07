package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 民宿实体类
 * 对应数据库homestay表，存储民宿的基本信息
 */
@TableName("homestay")
public class Homestay {
    /**
     * 民宿ID
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 民宿名称
     * 民宿的具体名称
     */
    private String name;
    
    /**
     * 地址
     * 民宿的详细地址
     */
    private String address;
    
    /**
     * 纬度
     * 民宿的地理纬度坐标
     */
    private Double latitude;
    
    /**
     * 经度
     * 民宿的地理经度坐标
     */
    private Double longitude;
    
    /**
     * 价格
     * 民宿的每晚价格
     */
    private Double price;
    
    /**
     * 房型
     * 民宿的房间类型
     */
    @TableField("room_type")
    private String roomType;
    
    /**
     * 设施
     * 民宿配备的设施
     */
    private String facility;

    /**
     * 图片URL
     * 民宿的封面图片地址
     */
    private String imageUrl;

    /**
     * 描述
     * 民宿的详细描述
     */
    private String description;

    /**
     * 状态
     * 民宿状态：0-下架，1-上架，2-待审核
     */
    private Integer status;
    
    /**
     * 房东ID
     * 民宿所有者的用户ID，关联user表
     */
    @TableField("owner_id")
    private Long ownerId;
    
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
     * 浏览量
     * 民宿的浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 推荐权重
     * 用于民宿推荐排序
     */
    @TableField("recommend_weight")
    private Integer recommendWeight;

    /**
     * 平均评分
     * 民宿的平均评分
     */
    @TableField("avg_rating")
    private Double avgRating;

    /**
     * 房东用户名
     * 非数据库字段，用于前端展示
     */
    @TableField(exist = false)
    private String landlordUsername;

    /**
     * 房东电话
     * 非数据库字段，用于前端展示
     */
    @TableField(exist = false)
    private String landlordPhone;

    /**
     * 获取民宿ID
     * 
     * @return Long 民宿ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置民宿ID
     * 
     * @param id 民宿ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取民宿名称
     * 
     * @return String 民宿名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置民宿名称
     * 
     * @param name 民宿名称
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取地址
     * 
     * @return String 地址
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * 设置地址
     * 
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
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
     * 获取房型
     * 
     * @return String 房型
     */
    public String getRoomType() {
        return roomType;
    }
    
    /**
     * 设置房型
     * 
     * @param roomType 房型
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    /**
     * 获取设施
     * 
     * @return String 设施
     */
    public String getFacility() {
        return facility;
    }
    
    /**
     * 设置设施
     * 
     * @param facility 设施
     */
    public void setFacility(String facility) {
        this.facility = facility;
    }

    /**
     * 获取图片URL
     * 
     * @return String 图片URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置图片URL
     * 
     * @param imageUrl 图片URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取描述
     * 
     * @return String 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     * 
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 获取房东用户名
     * 
     * @return String 房东用户名
     */
    public String getLandlordUsername() {
        return landlordUsername;
    }

    /**
     * 设置房东用户名
     * 
     * @param landlordUsername 房东用户名
     */
    public void setLandlordUsername(String landlordUsername) {
        this.landlordUsername = landlordUsername;
    }

    /**
     * 获取房东电话
     * 
     * @return String 房东电话
     */
    public String getLandlordPhone() {
        return landlordPhone;
    }

    /**
     * 设置房东电话
     * 
     * @param landlordPhone 房东电话
     */
    public void setLandlordPhone(String landlordPhone) {
        this.landlordPhone = landlordPhone;
    }

    /**
     * 获取浏览量
     * 
     * @return Integer 浏览量
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * 设置浏览量
     * 
     * @param viewCount 浏览量
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * 获取推荐权重
     * 
     * @return Integer 推荐权重
     */
    public Integer getRecommendWeight() {
        return recommendWeight;
    }

    /**
     * 设置推荐权重
     * 
     * @param recommendWeight 推荐权重
     */
    public void setRecommendWeight(Integer recommendWeight) {
        this.recommendWeight = recommendWeight;
    }

    /**
     * 获取平均评分
     * 
     * @return Double 平均评分
     */
    public Double getAvgRating() {
        return avgRating;
    }

    /**
     * 设置平均评分
     * 
     * @param avgRating 平均评分
     */
    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}