package com.homestay.service;

import com.homestay.entity.Homestay;
import com.homestay.utils.Result;

import java.util.Map;

/**
 * 民宿服务接口
 * 提供民宿相关的业务方法
 */
public interface HomestayService {

    /**
     * 获取民宿列表
     * <p>
     * 支持分页和多条件筛选，包括关键词搜索、价格范围、标签等
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含民宿列表的响应对象
     */
    Result getHomestayList(Map<String, Object> params);

    /**
     * 获取民宿详情
     * <p>
     * 根据民宿ID查询民宿详细信息
     * 
     * @param id 民宿ID
     * @return Result 包含民宿详情的响应对象
     */
    Result getHomestayDetail(Long id);

    /**
     * 创建民宿
     * <p>
     * 新增民宿信息到数据库
     * 
     * @param homestay 民宿信息对象
     * @return Result 创建结果的响应对象
     */
    Result createHomestay(Homestay homestay);

    /**
     * 更新民宿
     * <p>
     * 根据ID更新民宿信息
     * 
     * @param homestay 民宿信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    Result updateHomestay(Homestay homestay);

    /**
     * 删除民宿
     * <p>
     * 根据ID删除民宿信息
     * 
     * @param id 民宿ID
     * @return Result 删除结果的响应对象
     */
    Result deleteHomestay(Long id);

    /**
     * 搜索民宿
     * <p>
     * 根据关键词、价格等条件搜索民宿
     * 
     * @param params 搜索参数，包含关键词、价格范围等
     * @return Result 包含搜索结果的响应对象
     */
    Result searchHomestay(Map<String, Object> params);

    /**
     * 获取推荐民宿
     * <p>
     * 获取推荐的民宿列表，如热门民宿、评分高的民宿等
     * 
     * @return Result 包含推荐民宿列表的响应对象
     */
    Result getRecommendedHomestays();

    /**
     * 获取房东民宿
     * <p>
     * 获取当前登录房东的民宿列表
     * 
     * @return Result 包含房东民宿列表的响应对象
     */
    Result getLandlordHomestays();

    /**
     * 批量导入民宿
     * <p>
     * 批量将民宿信息导入数据库
     * 
     * @param homestays 民宿信息列表
     * @return Result 导入结果的响应对象
     */
    Result importHomestays(java.util.List<Homestay> homestays);
    
    /**
     * 审核民宿
     * <p>
     * 管理员审核民宿，设置审核状态
     * 
     * @param id 民宿ID
     * @param status 审核状态：1-通过，3-驳回
     * @param remark 审核备注
     * @return Result 审核结果的响应对象
     */
    Result reviewHomestay(Long id, Integer status, String remark);
    
    /**
     * 下架/上架民宿
     * <p>
     * 房东手动下架或上架民宿
     * 
     * @param id 民宿ID
     * @param status 状态：0-下架，1-上架
     * @return Result 操作结果的响应对象
     */
    Result updateHomestayStatus(Long id, Integer status);
    
    /**
     * 按名称和状态查询民宿
     * <p>
     * 房东按名称和状态查询自己的民宿
     * 
     * @param params 查询参数，包含名称关键词和状态
     * @return Result 包含民宿列表的响应对象
     */
    Result queryHomestayByNameAndStatus(Map<String, Object> params);
    
    /**
     * 获取待审核的民宿列表
     * <p>
     * 管理员获取待审核的民宿列表
     * 
     * @return Result 包含待审核民宿列表的响应对象
     */
    Result getPendingHomestays();
}
