package com.homestay.service.impl;

import com.homestay.entity.User;
import com.homestay.mapper.UserMapper;
import com.homestay.service.AuthService;
import com.homestay.utils.JwtUtils;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证服务实现类
 * 处理用户登录、注册、令牌刷新和登出等认证相关操作
 */
@Service
public class AuthServiceImpl implements AuthService {
    


    /**
     * 用户Mapper，用于数据库操作
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * JWT工具类，用于生成和验证令牌
     */
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 密码加密器，用于密码的加密和验证
     */
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户登录方法
     * @param loginId 登录标识符，可以是用户名或手机号
     * @param password 用户密码
     * @param role 角色信息
     * @return 登录结果，包含token和用户信息
     */
    @Override
    public Result login(String loginId, String password, String role) {
        // 根据登录标识符查询用户
        // 支持用户名和手机号两种登录方式
        User user = userMapper.selectByLoginId(loginId);
        
        // 用户不存在
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 密码验证
        // 支持明文密码和加密密码
        if (!password.equals(user.getPassword()) && !passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("密码错误");
        }

        // 角色验证
        // 将前端传递的角色转换为数据库角色（支持中文和英文）
        String requestedRole = null;
        if (role != null && !role.isEmpty()) {
            switch (role) {
                case "游客":
                case "TOURIST":
                    requestedRole = "TOURIST";
                    break;
                case "房东":
                case "LANDLORD":
                    requestedRole = "LANDLORD";
                    break;
                case "管理员":
                case "ADMIN":
                    requestedRole = "ADMIN";
                    break;
                default:
                    return Result.error("角色选择错误");
            }
        }

        // 验证前端选择的角色与数据库中存储的角色是否一致
        if (requestedRole != null && !requestedRole.equals(user.getRole())) {
            return Result.error("身份选择错误，请选择正确的登录身份");
        }

        // 生成JWT令牌
        // 确保使用非空值生成token，优先使用username，否则使用phone
        String tokenSubject = user.getUsername() != null ? user.getUsername() : user.getPhone();
        String accessToken = jwtUtils.generateAccessToken(tokenSubject, user.getId(), user.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(tokenSubject);
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("user", user);

        // 返回登录成功结果
        return Result.success("登录成功", data);
    }

    /**
     * 用户注册方法
     * @param user 用户注册信息
     * @return 注册结果
     */
    @Override
    public Result register(User user) {
        try {
            // 检查用户名是否已存在
            if (userMapper.selectByUsername(user.getUsername()) != null) {
                return Result.error("用户名已存在");
            }

            // 检查手机号是否已被注册
            if (userMapper.selectByPhone(user.getPhone()) != null) {
                return Result.error("手机号已被注册");
            }

            // 密码加密
            // 使用BCryptPasswordEncoder对密码进行加密存储
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // 处理角色字段
            // 尊重前端传递的role字段，转换为数据库枚举值
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("TOURIST"); // 默认为普通用户
            } else {
                // 将中文角色转换为数据库枚举值
                switch (user.getRole()) {
                    case "游客":
                    case "用户":
                        user.setRole("TOURIST");
                        break;
                    case "房东":
                        user.setRole("LANDLORD");
                        break;
                    case "管理员":
                        user.setRole("ADMIN");
                        break;
                    default:
                        user.setRole("TOURIST");
                        break;
                }
            }
            
            // 设置默认状态
            // 默认为启用状态（1），禁用状态为0
            if (user.getStatus() == null) {
                user.setStatus(1); // 默认为启用状态
            }

            // 移除对name字段的处理，因为数据库中不存在该字段

            // 插入用户
            // 调用MyBatis Plus的insert方法插入用户数据
            if (userMapper.insert(user) > 0) {
                return Result.success("注册成功");
            }

            // 插入失败
            return Result.error("注册失败");
        } catch (Exception e) {
            // 异常处理
            // 打印异常堆栈信息，方便调试
            e.printStackTrace();
            return Result.error("注册失败: " + e.getMessage());
        }
    }

    /**
     * 刷新令牌方法
     * @param refreshToken 刷新令牌
     * @return 刷新结果
     */
    @Override
    public Result refreshToken(String refreshToken) {
        // 验证刷新令牌
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            return Result.error("刷新令牌无效或已过期");
        }
        
        // 从刷新令牌中获取用户名
        String username = jwtUtils.getUsernameFromToken(refreshToken);
        if (username == null) {
            return Result.error("刷新令牌无效");
        }
        
        // 查询用户信息，获取角色
        // 使用selectByLoginId，支持用户名或手机号查询
        User user = userMapper.selectByLoginId(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        String newAccessToken = jwtUtils.generateAccessToken(username, user.getId(), user.getRole());
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", newAccessToken);
        
        // 返回刷新成功结果
        return Result.success("Token刷新成功", data);
    }

    /**
     * 用户登出方法
     * @return 登出结果
     */
    @Override
    public Result logout() {
        return Result.success("登出成功");
    }
    

    

}
