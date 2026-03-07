package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.entity.Rating;
import com.homestay.mapper.RatingMapper;
import com.homestay.service.RatingService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评分服务实现类
 * 实现RatingService接口，处理评分相关的业务逻辑
 */
@Service
public class RatingServiceImpl implements RatingService {

    /**
     * 评分Mapper，用于数据库操作
     */
    @Autowired
    private RatingMapper ratingMapper;

    /**
     * 创建评分
     * <p>
     * 新增评分信息到数据库
     * 
     * @param rating 评分信息对象
     * @return Result 创建结果的响应对象
     */
    @Override
    public Result createRating(Rating rating) {
        // 检查评分是否在有效范围内
        if (rating.getRating() < 1 || rating.getRating() > 5) {
            return Result.error("评分必须在1-5星之间");
        }

        // 检查是否已经评分
        QueryWrapper<Rating> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("homestay_id", rating.getHomestayId())
                .eq("user_id", rating.getUserId());
        if (ratingMapper.selectOne(queryWrapper) != null) {
            return Result.error("您已经对该民宿进行过评分");
        }

        // 保存评分
        if (ratingMapper.insert(rating) > 0) {
            return Result.success("评分成功", rating);
        }

        return Result.error("评分失败");
    }

    /**
     * 获取民宿的评分列表
     * <p>
     * 根据民宿ID查询该民宿的所有评分
     * 
     * @param homestayId 民宿ID
     * @return Result 包含评分列表的响应对象
     */
    @Override
    public Result getHomestayRatings(Long homestayId) {
        QueryWrapper<Rating> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("homestay_id", homestayId)
                .orderByDesc("create_time");
        return Result.success("获取评分列表成功", ratingMapper.selectList(queryWrapper));
    }

    /**
     * 获取民宿的平均评分
     * <p>
     * 根据民宿ID计算该民宿的平均评分
     * 
     * @param homestayId 民宿ID
     * @return Result 包含平均评分的响应对象
     */
    @Override
    public Result getHomestayAverageRating(Long homestayId) {
        Double averageRating = ratingMapper.selectAverageRatingByHomestayId(homestayId);
        if (averageRating == null) {
            averageRating = 0.0;
        }
        return Result.success("获取平均评分成功", averageRating);
    }

}
