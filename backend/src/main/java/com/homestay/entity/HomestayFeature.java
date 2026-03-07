package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 民宿特色实体类
 * 对应数据库homestay_feature表，存储民宿的特色信息
 */
@TableName("homestay_feature")
public class HomestayFeature {
    /**
     * 特色ID
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 民宿ID
     * 关联homestay表
     */
    private Long homestayId;
    
    /**
     * 特色描述
     * 民宿的详细特色描述
     */
    private String content;
    
    /**
     * 图片URL1
     * 特色图片1的网络地址
     */
    private String imageUrl1;
    
    /**
     * 图片URL2
     * 特色图片2的网络地址
     */
    private String imageUrl2;
    
    /**
     * 图片URL3
     * 特色图片3的网络地址
     */
    private String imageUrl3;
    
    /**
     * 标签
     * 民宿的特色标签
     */
    private String tag;
    
    /**
     * 状态
     * 特色信息审核状态：0-未审核，1-审核通过，2-审核驳回
     */
    private Integer status;
    
    /**
     * 获取特色ID
     * 
     * @return Long 特色ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置特色ID
     * 
     * @param id 特色ID
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取特色描述
     * 
     * @return String 特色描述
     */
    public String getContent() {
        return content;
    }
    
    /**
     * 设置特色描述
     * 
     * @param content 特色描述
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * 获取图片URL1
     * 
     * @return String 图片URL1
     */
    public String getImageUrl1() {
        return imageUrl1;
    }
    
    /**
     * 设置图片URL1
     * 
     * @param imageUrl1 图片URL1
     */
    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }
    
    /**
     * 获取图片URL2
     * 
     * @return String 图片URL2
     */
    public String getImageUrl2() {
        return imageUrl2;
    }
    
    /**
     * 设置图片URL2
     * 
     * @param imageUrl2 图片URL2
     */
    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }
    
    /**
     * 获取图片URL3
     * 
     * @return String 图片URL3
     */
    public String getImageUrl3() {
        return imageUrl3;
    }
    
    /**
     * 设置图片URL3
     * 
     * @param imageUrl3 图片URL3
     */
    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }
    
    /**
     * 获取标签
     * 
     * @return String 标签
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * 设置标签
     * 
     * @param tag 标签
     */
    public void setTag(String tag) {
        this.tag = tag;
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
}