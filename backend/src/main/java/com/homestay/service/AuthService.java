package com.homestay.service;

import com.homestay.entity.User;
import com.homestay.utils.Result;

/**
 * 认证服务接口
 * 提供用户认证相关的业务方法
 */
public interface AuthService {

    /**
     * 用户登录
     * <p>
     * 验证用户名和密码，生成JWT令牌
     * 
     * @param loginId 登录标识符（用户名或手机号）
     * @param password 密码
     * @param role 角色信息
     * @return Result 登录结果，包含令牌和用户信息
     */
    Result login(String loginId, String password, String role);

    /**
     * 用户注册
     * <p>
     * 注册新用户，加密密码，初始化用户信息
     * 
     * @param user 用户信息对象
     * @return Result 注册结果
     */
    Result register(User user);

    /**
     * 刷新令牌
     * <p>
     * 使用刷新令牌获取新的访问令牌
     * 
     * @param refreshToken 刷新令牌
     * @return Result 刷新结果，包含新的访问令牌
     */
    Result refreshToken(String refreshToken);

    /**
     * 用户登出
     * <p>
     * 处理用户登出逻辑，如清除令牌等
     * 
     * @return Result 登出结果
     */
    Result logout();
}
