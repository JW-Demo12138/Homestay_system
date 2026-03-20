package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestay.entity.Experience;

import java.util.List;

/**
 * 体验项目Mapper
 * 提供体验项目相关的数据库操作方法
 */
public interface ExperienceMapper extends BaseMapper<Experience> {
    
    /**
     * 根据房东ID查询体验项目
     * 
     * @param ownerId 房东ID
     * @return 体验项目列表
     */
    List<Experience> selectByOwnerId(Long ownerId);
    
    /**
     * 查询审核中的体验项目
     * 
     * @return 体验项目列表
     */
    List<Experience> selectPendingApproval();
    
    /**
     * 根据类型和状态查询体验项目
     * 
     * @param type 体验类型
     * @param status 状态
     * @return 体验项目列表
     */
    List<Experience> selectByTypeAndStatus(String type, Integer status);
}
