package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestay.entity.HomestayFeature;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 民宿特色Mapper接口
 * 继承BaseMapper，提供民宿特色相关的数据库操作方法
 */
public interface HomestayFeatureMapper extends BaseMapper<HomestayFeature> {
    /**
     * 根据标签获取专题数据
     * @param tag 特色标签
     * @return 专题数据列表
     */
    List<Map<String, Object>> selectFeatureTopic(@Param("tag") String tag);
}