package com.homestay.controller;

import com.homestay.entity.User;
import com.homestay.service.AuthService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册、令牌刷新和登出等认证相关的HTTP请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 认证服务，处理认证相关的业务逻辑
     */
    @Autowired
    private AuthService authService;

    /**
     * 用户登录接口
     * @param loginData 登录数据，包含用户名/手机号、密码和角色
     * @return 登录结果，包含token和用户信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> loginData) {
        // 从请求数据中获取登录标识符、密码和角色
        String loginId = loginData.get("loginId");
        String username = loginData.get("username");
        String phone = loginData.get("phone");
        String password = loginData.get("password");
        String role = loginData.get("role");
        
        // 确定登录标识符
        // 优先使用loginId，如果没有则使用username，如果没有则使用phone
        if (loginId == null) {
            loginId = username != null ? username : phone;
        }
        
        // 调用认证服务进行登录，传递角色信息
        return authService.login(loginId, password, role);
    }

    /**
     * 用户注册接口
     * @param user 用户注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 调用认证服务进行注册
        return authService.register(user);
    }

    /**
     * 刷新令牌接口
     * @param tokenData 令牌数据，包含刷新令牌
     * @return 刷新结果，包含新的访问令牌
     */
    @PostMapping("/refresh")
    public Result refreshToken(@RequestBody Map<String, String> tokenData) {
        // 从请求数据中获取刷新令牌
        String refreshToken = tokenData.get("refreshToken");
        
        // 调用认证服务刷新令牌
        return authService.refreshToken(refreshToken);
    }

    /**
     * 用户登出接口
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result logout() {
        // 调用认证服务进行登出
        return authService.logout();
    }
    

    

}
