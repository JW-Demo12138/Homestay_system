package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 用户实体类
 * 对应数据库user表，存储用户的基本信息
 */
@TableName("user")
public class User {
    /**
     * 用户ID
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     * 用户登录名，唯一
     */
    @TableField("username")
    private String username;
    
    /**
     * 密码
     * 加密存储的用户密码
     */
    @TableField("password")
    private String password;
    
    /**
     * 手机号
     * 用户联系电话，唯一
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 姓名
     * 用户真实姓名
     */
    @TableField("name")
    private String name;
    
    /**
     * 邮箱
     * 用户邮箱地址
     */
    @TableField("email")
    private String email;
    
    /**
     * 角色
     * 用户角色：LANDLORD（房东）、TOURIST（游客）、ADMIN（管理员）
     */
    @TableField("role")
    private String role;
    
    /**
     * 状态
     * 用户状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;
    
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
     * 头像URL
     * 用户头像的网络地址
     */
    @TableField("avatar")
    private String avatar;
    
    /**
     * 获取用户ID
     * 
     * @return Long 用户ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置用户ID
     * 
     * @param id 用户ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取用户名
     * 
     * @return String 用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * 
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取密码
     * 
     * @return String 密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置密码
     * 
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取手机号
     * 
     * @return String 手机号
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * 设置手机号
     * 
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * 获取姓名
     * 
     * @return String 姓名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置姓名
     * 
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取邮箱
     * 
     * @return String 邮箱
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 设置邮箱
     * 
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 获取角色
     * 
     * @return String 角色
     */
    public String getRole() {
        return role;
    }
    
    /**
     * 设置角色
     * 
     * @param role 角色
     */
    public void setRole(String role) {
        this.role = role;
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
     * 获取头像URL
     * 
     * @return String 头像URL
     */
    public String getAvatar() {
        return avatar;
    }
    
    /**
     * 设置头像URL
     * 
     * @param avatar 头像URL
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}