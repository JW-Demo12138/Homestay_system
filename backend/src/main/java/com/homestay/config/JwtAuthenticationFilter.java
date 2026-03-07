package com.homestay.config;

import com.homestay.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 用于解析JWT令牌并设置用户认证信息到SecurityContext中
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 清除旧的认证上下文，保证每次请求重新认证
        SecurityContextHolder.clearContext();
        
        // 从请求头中获取Authorization
        String authorizationHeader = request.getHeader("Authorization");
        
        // 检查Authorization头是否存在且格式正确
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 提取token
            String token = authorizationHeader.substring(7);
            
            // 解析token获取Claims
            Claims claims = jwtUtils.getClaimsByToken(token);
            
            // 检查token是否有效
            if (claims == null) {
                // token无效，返回401错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"code\": 401, \"message\": \"Token无效\"}");
                return;
            }
            
            // 检查token是否过期
            if (jwtUtils.isTokenExpired(claims)) {
                // token过期，返回401错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"code\": 401, \"message\": \"Token已过期\"}");
                return;
            }
            
            // 解析token获取用户名和角色
            String username = jwtUtils.getUsernameFromToken(token);
            String role = jwtUtils.getRoleFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);
            
            if (username != null && role != null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(authority)
                );
                
                authentication.setDetails(userId);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // 继续处理请求
        chain.doFilter(request, response);
    }
}
