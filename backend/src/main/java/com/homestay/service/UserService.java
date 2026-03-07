package com.homestay.service;

import com.homestay.entity.User;
import com.homestay.utils.Result;

import java.util.Map;

/**
 * 用户服务接口
 * 提供用户相关的业务方法
 */
public interface UserService {

    /**
     * 获取用户信息
     * <p>
     * 获取当前登录用户的详细信息
     * 
     * @return Result 包含用户信息的响应对象
     */
    Result getUserInfo();

    /**
     * 更新用户信息
     * <p>
     * 更新当前登录用户的基本信息
     * 
     * @param user 用户信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    Result updateUserInfo(User user);

    /**
     * 修改密码
     * <p>
     * 验证旧密码后，更新用户密码
     * 
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result 密码修改结果的响应对象
     */
    Result changePassword(String oldPassword, String newPassword);

    /**
     * 更新头像
     * <p>
     * 更新当前登录用户的头像URL
     * 
     * @param avatarUrl 头像URL
     * @return Result 更新结果的响应对象
     */
    Result updateAvatar(String avatarUrl);

    /**
     * 获取用户列表（管理员）
     * <p>
     * 管理员获取所有用户列表，支持分页和筛选
     * 
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含用户列表的响应对象
     */
    Result getUserList(Map<String, Object> params);

    /**
     * 管理员更新用户信息
     * <p>
     * 管理员更新指定用户的信息，包括角色等权限相关字段
     * 
     * @param user 用户信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    Result updateUserByAdmin(User user);

    /**
     * 管理员删除用户
     * <p>
     * 管理员删除指定用户
     * 
     * @param id 用户ID
     * @return Result 删除结果的响应对象
     */
    Result deleteUserByAdmin(Long id);

    Result updateUserStatus(Long id, Integer status);
}
