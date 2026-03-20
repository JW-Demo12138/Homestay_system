package com.homestay.service;

import com.homestay.entity.Experience;
import com.homestay.utils.Result;

import java.util.Map;

/**
 * 乡村特色体验服务接口
 * 提供体验项目相关的业务方法
 */
public interface ExperienceService {

    /**
     * 获取体验项目列表
     * <p>
     * 支持分页和多条件筛选，包括类型、价格范围等
     * 
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含体验项目列表的响应对象
     */
    Result getExperienceList(Map<String, Object> params);

    /**
     * 获取体验项目详情
     * <p>
     * 根据体验项目ID查询详细信息
     * 
     * @param id 体验项目ID
     * @return Result 包含体验项目详情的响应对象
     */
    Result getExperienceDetail(Long id);

    /**
     * 创建体验项目
     * <p>
     * 新增体验项目信息到数据库
     * 
     * @param experience 体验项目信息对象
     * @return Result 创建结果的响应对象
     */
    Result createExperience(Experience experience);

    /**
     * 更新体验项目
     * <p>
     * 根据ID更新体验项目信息
     * 
     * @param experience 体验项目信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    Result updateExperience(Experience experience);

    /**
     * 删除体验项目
     * <p>
     * 根据ID删除体验项目信息
     * 
     * @param id 体验项目ID
     * @return Result 删除结果的响应对象
     */
    Result deleteExperience(Long id);

    /**
     * 审核体验项目
     * <p>
     * 管理员审核体验项目，设置审核状态
     * 
     * @param id 体验项目ID
     * @param status 审核状态：1-通过，3-驳回
     * @param remark 审核备注
     * @return Result 审核结果的响应对象
     */
    Result reviewExperience(Long id, Integer status, String remark);

    /**
     * 下架/上架体验项目
     * <p>
     * 房东手动下架或上架体验项目
     * 
     * @param id 体验项目ID
     * @param status 状态：0-下架，1-上架
     * @return Result 操作结果的响应对象
     */
    Result updateExperienceStatus(Long id, Integer status);

    /**
     * 按类型和状态查询体验项目
     * <p>
     * 游客按类型和状态查询体验项目
     * 
     * @param type 体验类型
     * @param status 状态
     * @return Result 包含体验项目列表的响应对象
     */
    Result getExperienceByTypeAndStatus(String type, Integer status);

    /**
     * 获取房东的体验项目列表
     * <p>
     * 获取当前登录房东的体验项目列表
     * 
     * @return Result 包含房东体验项目列表的响应对象
     */
    Result getLandlordExperiences();

    /**
     * 获取待审核的体验项目列表
     * <p>
     * 管理员获取待审核的体验项目列表
     * 
     * @return Result 包含待审核体验项目列表的响应对象
     */
    Result getPendingExperiences();
}
