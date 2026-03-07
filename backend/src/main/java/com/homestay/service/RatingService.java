package com.homestay.service;

import com.homestay.entity.Rating;
import com.homestay.utils.Result;

import java.util.Map;

/**
 * 评分服务接口
 * 提供评分相关的业务方法
 */
public interface RatingService {

    /**
     * 创建评分
     * <p>
     * 新增评分信息到数据库
     *
     * @param rating 评分信息对象
     * @return Result 创建结果的响应对象
     */
    Result createRating(Rating rating);

    /**
     * 获取民宿的评分列表
     * <p>
     * 根据民宿ID查询该民宿的所有评分
     *
     * @param homestayId 民宿ID
     * @return Result 包含评分列表的响应对象
     */
    Result getHomestayRatings(Long homestayId);

    /**
     * 获取民宿的平均评分
     * <p>
     * 根据民宿ID计算该民宿的平均评分
     *
     * @param homestayId 民宿ID
     * @return Result 包含平均评分的响应对象
     */
    Result getHomestayAverageRating(Long homestayId);

}
