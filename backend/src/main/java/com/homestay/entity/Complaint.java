package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 投诉实体类
 * 对应数据库complaint表，存储用户对民宿的投诉信息
 */
@TableName("complaint")
public class Complaint {
    /**
     * 投诉ID
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     * 投诉人ID，关联user表
     */
    private Long userId;
    
    /**
     * 民宿ID
     * 被投诉的民宿ID，关联homestay表
     */
    private Long homestayId;
    
    /**
     * 投诉内容
     * 详细的投诉描述
     */
    private String content;
    
    /**
     * 状态
     * 投诉处理状态：0-待处理，1-已处理
     */
    private Integer status;
    
    /**
     * 处理时间
     * 投诉被处理的时间
     */
    private Date handleTime;
    
    /**
     * 创建时间
     * 投诉提交的时间
     */
    private Date createTime;
    
    /**
     * 获取投诉ID
     * 
     * @return Long 投诉ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置投诉ID
     * 
     * @param id 投诉ID
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
     * 获取投诉内容
     * 
     * @return String 投诉内容
     */
    public String getContent() {
        return content;
    }
    
    /**
     * 设置投诉内容
     * 
     * @param content 投诉内容
     */
    public void setContent(String content) {
        this.content = content;
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
     * 获取处理时间
     * 
     * @return Date 处理时间
     */
    public Date getHandleTime() {
        return handleTime;
    }
    
    /**
     * 设置处理时间
     * 
     * @param handleTime 处理时间
     */
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
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
}