package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestay.entity.Rating;

/**
 * 评分Mapper接口
 * 继承BaseMapper，提供评分相关的数据库操作方法
 */
public interface RatingMapper extends BaseMapper<Rating> {

    /**
     * 根据民宿ID查询平均评分
     * <p>
     * 根据民宿ID计算该民宿的平均评分
     * 
     * @param homestayId 民宿ID
     * @return Double 平均评分
     */
    Double selectAverageRatingByHomestayId(Long homestayId);

}
