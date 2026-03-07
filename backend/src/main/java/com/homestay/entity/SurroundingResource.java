package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 周边资源实体类
 * 对应数据库surrounding_resource表，存储民宿周边的旅游资源信息
 */
@TableName("surrounding_resource")
public class SurroundingResource {
    /**
     * 资源ID
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 资源名称
     * 周边资源的名称
     */
    private String name;
    
    /**
     * 资源类型
     * 周边资源的类型，如景点、采摘区等
     */
    private String type;
    
    /**
     * 纬度
     * 周边资源的地理纬度坐标
     */
    private Double latitude;
    
    /**
     * 经度
     * 周边资源的地理经度坐标
     */
    private Double longitude;
    
    /**
     * 民宿ID
     * 关联的民宿ID，关联homestay表
     */
    private Long homestayId;
    
    /**
     * 获取资源ID
     * 
     * @return Long 资源ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置资源ID
     * 
     * @param id 资源ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取资源名称
     * 
     * @return String 资源名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置资源名称
     * 
     * @param name 资源名称
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取资源类型
     * 
     * @return String 资源类型
     */
    public String getType() {
        return type;
    }
    
    /**
     * 设置资源类型
     * 
     * @param type 资源类型
     */
    public void setType(String type) {
        this.type = type;
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
}