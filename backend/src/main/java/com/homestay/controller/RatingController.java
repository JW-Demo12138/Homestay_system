package com.homestay.controller;

import com.homestay.entity.Rating;
import com.homestay.service.RatingService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 评分控制器
 * 处理评分相关的HTTP请求，包括创建评分、获取评分列表和获取平均评分等操作
 */
@RestController
@RequestMapping("/api/rating")
public class RatingController {

    /**
     * 评分服务，处理评分相关的业务逻辑
     */
    @Autowired
    private RatingService ratingService;

    /**
     * 创建评分接口
     * @param rating 评分信息
     * @return 评分结果
     */
    @PostMapping("/create")
    public Result createRating(@RequestBody Rating rating) {
        // 调用评分服务创建评分
        return ratingService.createRating(rating);
    }

    /**
     * 获取民宿评分列表接口
     * @param homestayId 民宿ID
     * @return 评分列表结果
     */
    @GetMapping("/homestay/{id}")
    public Result getHomestayRatings(@PathVariable("id") Long homestayId) {
        // 调用评分服务获取民宿评分列表
        return ratingService.getHomestayRatings(homestayId);
    }

    /**
     * 获取民宿平均评分接口
     * @param homestayId 民宿ID
     * @return 平均评分结果
     */
    @GetMapping("/average/{id}")
    public Result getHomestayAverageRating(@PathVariable("id") Long homestayId) {
        // 调用评分服务获取民宿平均评分
        return ratingService.getHomestayAverageRating(homestayId);
    }

    /**
     * 管理员查询所有评价
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词（可选）
     * @return 评价列表
     */
    @GetMapping("/admin/list")
    public Result getAllRatings(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(required = false) String keyword) {
        return ratingService.getAllRatings(page, size, keyword);
    }

    /**
     * 管理员删除评价
     * @param id 评价ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/delete/{id}")
    public Result deleteRating(@PathVariable Long id) {
        return ratingService.deleteRating(id);
    }

}
