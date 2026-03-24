package com.homestay.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT工具类
 * 用于生成、解析和验证JWT令牌
 */
@Component
public class JwtUtils {

    /**
     * 预生成的安全密钥
     * 使用Keys.secretKeyFor生成，确保足够安全
     */
    private final Key key;

    /**
     * 访问令牌过期时间（秒）
     * 令牌的有效时长
     * 个人项目设置为30天，避免频繁登录
     */
    private final long accessTokenExpire = 2592000; // 30天，单位秒
    
    /**
     * 刷新令牌过期时间（秒）
     * 刷新令牌的有效时长
     * 个人项目设置为90天，长期保持登录状态
     */
    private final long refreshTokenExpire = 7776000; // 90天，单位秒

    /**
     * 构造方法
     * 初始化安全密钥
     */
    public JwtUtils() {
        // 使用固定的密钥，确保应用重启后仍能验证之前生成的令牌
        // 密钥需要足够长且复杂，这里使用一个256位的密钥
        String secretKey = "homestay-secret-key-2024-very-secure-and-long-enough-for-jwt-signing";
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 生成访问令牌
     * <p>
     * 根据用户名和角色生成JWT访问令牌
     * 
     * @param username 用户名
     * @param role     角色
     * @return String JWT访问令牌
     */
    public String generateAccessToken(String username, Long userId, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpire * 1000);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    /**
     * 生成刷新令牌
     * <p>
     * 根据用户名生成JWT刷新令牌
     * 
     * @param username 用户名
     * @return String JWT刷新令牌
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpire * 1000);

        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    /**
     * 从令牌中获取Claims
     * <p>
     * 解析JWT令牌，获取其中的Claims对象
     * 
     * @param token JWT令牌
     * @return Claims 令牌中的Claims对象，解析失败返回null
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 解析失败，返回null
            return null;
        }
    }

    /**
     * 检查令牌是否过期
     * <p>
     * 判断令牌的过期时间是否早于当前时间
     * 
     * @param claims Claims对象
     * @return boolean 是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 从令牌中获取用户名
     * <p>
     * 解析令牌并获取其中的用户名
     * 
     * @param token JWT令牌
     * @return String 用户名，解析失败返回null
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 从令牌中获取角色
     * <p>
     * 解析令牌并获取其中的角色信息
     * 
     * @param token JWT令牌
     * @return String 角色，解析失败返回null
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims != null) {
            return (String) claims.get("role");
        }
        return null;
    }
    
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims != null) {
            Object userId = claims.get("userId");
            if (userId != null) {
                return Long.valueOf(userId.toString());
            }
        }
        return null;
    }
    
    /**
     * 从令牌中获取令牌类型
     * <p>
     * 解析令牌并获取其中的令牌类型
     * 
     * @param token JWT令牌
     * @return String 令牌类型，解析失败返回null
     */
    public String getTokenType(String token) {
        Claims claims = getClaimsByToken(token);
        if (claims != null) {
            return (String) claims.get("type");
        }
        return null;
    }
    
    /**
     * 验证刷新令牌
     * <p>
     * 验证刷新令牌的有效性
     * 
     * @param refreshToken 刷新令牌
     * @return boolean 是否有效
     */
    public boolean validateRefreshToken(String refreshToken) {
        Claims claims = getClaimsByToken(refreshToken);
        if (claims == null) {
            return false;
        }
        
        // 检查令牌类型
        String type = (String) claims.get("type");
        if (!"refresh".equals(type)) {
            return false;
        }
        
        // 检查是否过期
        return !isTokenExpired(claims);
    }
}

