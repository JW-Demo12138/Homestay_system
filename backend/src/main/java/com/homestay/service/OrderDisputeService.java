package com.homestay.service;

import com.homestay.entity.OrderDispute;
import com.homestay.utils.Result;

import java.util.Map;

/**
 * 订单纠纷服务接口
 */
public interface OrderDisputeService {

    /**
     * 创建纠纷
     * @param dispute 纠纷信息
     * @return Result 创建结果的响应对象
     */
    Result createDispute(OrderDispute dispute);

    /**
     * 获取当前用户的纠纷列表
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含纠纷列表的响应对象
     */
    Result getUserDisputes(Map<String, Object> params);

    /**
     * 获取所有纠纷列表（管理员）
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含纠纷列表的响应对象
     */
    Result getAllDisputes(Map<String, Object> params);

    /**
     * 获取纠纷详情
     * @param id 纠纷ID
     * @return Result 包含纠纷详情的响应对象
     */
    Result getDisputeDetail(Long id);

    /**
     * 处理纠纷
     * @param id 纠纷ID
     * @param status 纠纷状态
     * @param handleResult 处理结果
     * @return Result 处理结果的响应对象
     */
    Result handleDispute(Long id, String status, String handleResult);

    /**
     * 用户撤销纠纷
     * @param id 纠纷ID
     * @return Result 撤销结果的响应对象
     */
    Result cancelDispute(Long id);
}
