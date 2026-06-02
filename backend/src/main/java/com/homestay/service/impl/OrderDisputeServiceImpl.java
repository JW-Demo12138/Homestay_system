package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Order;
import com.homestay.entity.OrderDispute;
import com.homestay.mapper.OrderDisputeMapper;
import com.homestay.mapper.OrderMapper;
import com.homestay.service.OrderDisputeService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 订单纠纷服务实现类
 */
@Service
public class OrderDisputeServiceImpl implements OrderDisputeService {

    @Autowired
    private OrderDisputeMapper orderDisputeMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result createDispute(OrderDispute dispute) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        Order order = orderMapper.selectById(dispute.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作此订单");
        }

        dispute.setUserId(userId);
        dispute.setHomestayId(order.getHomestayId());
        dispute.setStatus("PENDING");

        if (orderDisputeMapper.insert(dispute) > 0) {
            return Result.success("创建纠纷成功", dispute);
        }
        return Result.error("创建纠纷失败");
    }

    @Override
    public Result getUserDisputes(Map<String, Object> params) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<OrderDispute> disputePage = new Page<>(page, size);
        QueryWrapper<OrderDispute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("create_time");

        if (params.get("status") != null) {
            queryWrapper.eq("status", params.get("status"));
        }

        IPage<OrderDispute> result = orderDisputeMapper.selectPage(disputePage, queryWrapper);
        return Result.success("获取纠纷列表成功", result);
    }

    @Override
    public Result getAllDisputes(Map<String, Object> params) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<OrderDispute> disputePage = new Page<>(page, size);
        QueryWrapper<OrderDispute> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        if (params.get("status") != null) {
            queryWrapper.eq("status", params.get("status"));
        }

        if (params.get("disputeType") != null) {
            queryWrapper.eq("dispute_type", params.get("disputeType"));
        }

        if (params.get("keyword") != null) {
            queryWrapper.like("dispute_title", params.get("keyword"))
                    .or().like("dispute_content", params.get("keyword"));
        }

        IPage<OrderDispute> result = orderDisputeMapper.selectPage(disputePage, queryWrapper);
        return Result.success("获取纠纷列表成功", result);
    }

    @Override
    public Result getDisputeDetail(Long id) {
        OrderDispute dispute = orderDisputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }

        Long userId = SecurityUtils.getCurrentUserId();
        if (userId != null && !userId.equals(dispute.getUserId()) && !SecurityUtils.isAdmin()) {
            return Result.error("无权查看此纠纷");
        }

        return Result.success("获取纠纷详情成功", dispute);
    }

    @Override
    public Result handleDispute(Long id, String status, String handleResult) {
        OrderDispute dispute = orderDisputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }

        Long handleUserId = SecurityUtils.getCurrentUserId();
        if (handleUserId == null) {
            return Result.error("用户未登录");
        }

        dispute.setStatus(status);
        dispute.setHandleResult(handleResult);
        dispute.setHandleTime(LocalDateTime.now());
        dispute.setHandleUserId(handleUserId);

        if (orderDisputeMapper.updateById(dispute) > 0) {
            return Result.success("处理纠纷成功", dispute);
        }
        return Result.error("处理纠纷失败");
    }

    @Override
    public Result cancelDispute(Long id) {
        OrderDispute dispute = orderDisputeMapper.selectById(id);
        if (dispute == null) {
            return Result.error("纠纷不存在");
        }

        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null || !userId.equals(dispute.getUserId())) {
            return Result.error("无权操作此纠纷");
        }

        if (!"PENDING".equals(dispute.getStatus()) && !"PROCESSING".equals(dispute.getStatus())) {
            return Result.error("当前状态无法撤销");
        }

        dispute.setStatus("CLOSED");
        if (orderDisputeMapper.updateById(dispute) > 0) {
            return Result.success("撤销纠纷成功", dispute);
        }
        return Result.error("撤销纠纷失败");
    }
}
