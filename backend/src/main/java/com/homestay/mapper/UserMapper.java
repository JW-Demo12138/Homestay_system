package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestay.entity.User;

/**
 * 用户Mapper接口
 * 继承BaseMapper，提供用户相关的数据库操作方法
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * <p>
     * 根据用户名从数据库中查询用户信息
     * 
     * @param username 用户名
     * @return User 用户信息对象，不存在返回null
     */
    User selectByUsername(String username);

    /**
     * 根据手机号查询用户
     * <p>
     * 根据手机号从数据库中查询用户信息
     * 
     * @param phone 手机号
     * @return User 用户信息对象，不存在返回null
     */
    User selectByPhone(String phone);

    /**
     * 根据登录ID查询用户
     * <p>
     * 根据登录ID（用户名或手机号）从数据库中查询用户信息
     * 
     * @param loginId 登录ID（用户名或手机号）
     * @return User 用户信息对象，不存在返回null
     */
    User selectByLoginId(String loginId);

}