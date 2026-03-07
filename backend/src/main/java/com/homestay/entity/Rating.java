package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 评分实体类
 * 对应数据库homestay_rating表，存储用户对民宿的评分信息
 */
@TableName("homestay_rating")
public class Rating {
    /**
     * 评分ID
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long ratingId;
    
    /**
     * 民宿ID
     * 被评分的民宿ID，关联homestay表
     */
    private Long homestayId;
    
    /**
     * 用户ID
     * 评分人ID，关联user_info表
     */
    private Long userId;
    
    /**
     * 评分
     * 评分：1-5星
     */
    private Integer rating;
    
    /**
     * 评论内容
     * 详细的评论描述
     */
    private String comment;
    
    /**
     * 创建时间
     * 评分提交的时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     * 评分更新的时间
     */
    private Date updateTime;
    
    /**
     * 获取评分ID
     * 
     * @return Long 评分ID
     */
    public Long getRatingId() {
        return ratingId;
    }
    
    /**
     * 设置评分ID
     * 
     * @param ratingId 评分ID
     */
    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
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
     * 获取评分
     * 
     * @return Integer 评分
     */
    public Integer getRating() {
        return rating;
    }
    
    /**
     * 设置评分
     * 
     * @param rating 评分
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    /**
     * 获取评论内容
     * 
     * @return String 评论内容
     */
    public String getComment() {
        return comment;
    }
    
    /**
     * 设置评论内容
     * 
     * @param comment 评论内容
     */
    public void setComment(String comment) {
        this.comment = comment;
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
