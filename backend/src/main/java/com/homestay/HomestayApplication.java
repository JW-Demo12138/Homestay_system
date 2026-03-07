package com.homestay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 民宿系统应用启动类
 * 应用的入口点，配置了MyBatis Mapper扫描和跨域处理
 */
@SpringBootApplication
@EnableCaching // 启用缓存
@MapperScan("com.homestay.mapper") // 扫描Mapper接口
public class HomestayApplication {

    /**
     * 应用程序主入口
     * <p>
     * 启动Spring Boot应用程序
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(HomestayApplication.class, args);
    }

    /**
     * 配置跨域过滤器
     * <p>
     * 配置CORS跨域访问，允许来自http://localhost:3000的请求
     * 
     * @return CorsFilter 跨域过滤器实例
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 明确允许localhost:3000
        config.addAllowedOrigin("http://localhost:3000");
        // 允许所有头部，包括Authorization
        config.addAllowedHeader("*");
        // 允许所有HTTP方法
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        // 预检请求有效期
        config.setMaxAge(3600L);
        // 应用到所有路径
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
