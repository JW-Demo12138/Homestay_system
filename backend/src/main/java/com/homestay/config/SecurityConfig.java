package com.homestay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 用于配置应用的安全策略，包括请求权限、跨域设置等
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置HTTP安全策略
     * @param http HttpSecurity对象，用于配置安全策略
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护
            .csrf().disable()
            
            // 允许跨域请求
            .cors()
            .and()
            
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 配置请求权限
            .authorizeRequests()
            
            // 公开接口
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/upload/**").permitAll() // 放行上传接口
            .antMatchers("/api/geocoding/**").permitAll()
            .antMatchers("/api/test/**").permitAll() // 放行测试接口
            .antMatchers("/api/city/**").permitAll()
            .antMatchers("/api/cities/**").permitAll()
            .antMatchers("/api/tag/**").permitAll()
            .antMatchers("/api/tags/**").permitAll()
            
            // 民宿相关接口
            .antMatchers("/api/homestay/**").permitAll()
            
            // 特色专题接口
            .antMatchers("/api/feature/topic").permitAll()
            .antMatchers("/api/feature/list").permitAll()
            
            .antMatchers("/uploads/**").permitAll()
            .antMatchers("/static/**").permitAll()
            
            // 其他接口必须登录
            .anyRequest().authenticated();

        return http.build();
    }
}