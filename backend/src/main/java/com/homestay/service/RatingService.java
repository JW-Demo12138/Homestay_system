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

    /**
     * 获取所有评分列表（管理员）
     * <p>
     * 管理员获取所有评分列表，支持分页和筛选
     *
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含评分列表的响应对象
     */
    Result getAllRatings(Map<String, Object> params);

    /**
     * 获取所有评分列表（管理员）- 带分页参数
     * <p>
     * 管理员获取所有评分列表，支持分页和关键词搜索
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @return Result 包含评分列表的响应对象
     */
    Result getAllRatings(Integer page, Integer size, String keyword);

    /**
     * 管理员删除违规评价
     * <p>
     * 管理员可以删除违规的评价
     *
     * @param ratingId 评分ID
     * @return Result 删除结果的响应对象
     */
    Result deleteRating(Long ratingId);
}
