package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.User;
import com.homestay.mapper.UserMapper;
import com.homestay.service.UserService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户服务实现类
 * 实现UserService接口，处理用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 获取当前登录用户ID
     * <p>
     * 从Spring Security上下文获取当前登录用户的ID
     * 
     * @return Long 当前登录用户的ID
     */
    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * 获取用户信息
     * <p>
     * 获取当前登录用户的详细信息
     * 
     * @return Result 包含用户信息的响应对象
     */
    @Override
    public Result getUserInfo() {
        // 根据当前登录用户ID查询用户信息
        Long userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success("获取用户信息成功", user);
    }

    /**
     * 更新用户信息
     * <p>
     * 更新当前登录用户的基本信息
     * 
     * @param user 用户信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    @Override
    public Result updateUserInfo(User user) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        User existingUser = userMapper.selectById(currentUserId);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        
        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            User checkUser = userMapper.selectByUsername(user.getUsername());
            if (checkUser != null) {
                return Result.error("用户名已存在");
            }
            existingUser.setUsername(user.getUsername());
        }
        
        if (user.getPhone() != null && !user.getPhone().equals(existingUser.getPhone())) {
            User checkUser = userMapper.selectByPhone(user.getPhone());
            if (checkUser != null) {
                return Result.error("手机号已被注册");
            }
            existingUser.setPhone(user.getPhone());
        }
        
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        
        if (userMapper.updateById(existingUser) > 0) {
            return Result.success("更新用户信息成功", existingUser);
        }
        return Result.error("更新用户信息失败");
    }

    /**
     * 修改密码
     * <p>
     * 验证旧密码后，更新用户密码
     * 
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result 密码修改结果的响应对象
     */
    @Override
    public Result changePassword(String oldPassword, String newPassword) {
        // 根据当前登录用户ID查询用户信息
        Long userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error("旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        if (userMapper.updateById(user) > 0) {
            return Result.success("修改密码成功");
        }
        return Result.error("修改密码失败");
    }

    /**
     * 更新头像
     * <p>
     * 更新当前登录用户的头像URL
     * 
     * @param avatarUrl 头像URL
     * @return Result 更新结果的响应对象
     */
    @Override
    public Result updateAvatar(String avatarUrl) {
        // 根据当前登录用户ID更新头像
        Long userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setAvatar(avatarUrl);
        if (userMapper.updateById(user) > 0) {
            return Result.success("更新头像成功", user);
        }
        return Result.error("更新头像失败");
    }

    /**
     * 获取用户列表（管理员）
     * <p>
     * 管理员获取所有用户列表，支持分页和筛选
     * 
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含用户列表的响应对象
     */
    @Override
    public Result getUserList(Map<String, Object> params) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<User> userPage = new Page<>(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 添加搜索条件
        if (params.get("keyword") != null) {
            queryWrapper.like("username", params.get("keyword"))
                    .or().like("nickname", params.get("keyword"));
        }

        if (params.get("role") != null) {
            queryWrapper.eq("role", params.get("role"));
        }

        IPage<User> result = userMapper.selectPage(userPage, queryWrapper);
        return Result.success("获取用户列表成功", result);
    }

    /**
     * 管理员更新用户信息
     * <p>
     * 管理员更新指定用户的信息，包括角色等权限相关字段
     * 
     * @param user 用户信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    @Override
    public Result updateUserByAdmin(User user) {
        if (userMapper.updateById(user) > 0) {
            return Result.success("更新用户信息成功", user);
        }
        return Result.error("更新用户信息失败");
    }

    /**
     * 管理员删除用户
     * <p>
     * 管理员删除指定用户
     * 
     * @param id 用户ID
     * @return Result 删除结果的响应对象
     */
    @Override
    public Result deleteUserByAdmin(Long id) {
        if (userMapper.deleteById(id) > 0) {
            return Result.success("删除用户成功");
        }
        return Result.error("删除用户失败");
    }

    @Override
    public Result updateUserStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        if (userMapper.updateById(user) > 0) {
            return Result.success("更新用户状态成功");
        }
        return Result.error("更新用户状态失败");
    }
}
