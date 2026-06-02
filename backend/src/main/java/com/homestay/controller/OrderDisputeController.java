package com.homestay.controller;

import com.homestay.entity.OrderDispute;
import com.homestay.service.OrderDisputeService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单纠纷控制器
 * 处理订单纠纷相关的HTTP请求
 */
@RestController
@RequestMapping("/api/order-dispute")
public class OrderDisputeController {

    @Autowired
    private OrderDisputeService orderDisputeService;

    /**
     * 创建纠纷
     * @param dispute 纠纷信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result createDispute(@RequestBody OrderDispute dispute) {
        return orderDisputeService.createDispute(dispute);
    }

    /**
     * 获取当前用户的纠纷列表
     * @param params 查询参数，包含分页信息和筛选条件
     * @return 纠纷列表结果
     */
    @GetMapping("/user/list")
    public Result getUserDisputes(@RequestParam Map<String, Object> params) {
        return orderDisputeService.getUserDisputes(params);
    }

    /**
     * 获取所有纠纷列表（管理员）
     * @param params 查询参数，包含分页信息和筛选条件
     * @return 纠纷列表结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/list")
    public Result getAllDisputes(@RequestParam Map<String, Object> params) {
        return orderDisputeService.getAllDisputes(params);
    }

    /**
     * 获取纠纷详情
     * @param id 纠纷ID
     * @return 纠纷详情结果
     */
    @GetMapping("/detail/{id}")
    public Result getDisputeDetail(@PathVariable Long id) {
        return orderDisputeService.getDisputeDetail(id);
    }

    /**
     * 处理纠纷
     * @param id 纠纷ID
     * @param handleData 处理数据，包含status和handleResult
     * @return 处理结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/handle/{id}")
    public Result handleDispute(@PathVariable Long id, @RequestBody Map<String, String> handleData) {
        String status = handleData.get("status");
        String handleResult = handleData.get("handleResult");
        return orderDisputeService.handleDispute(id, status, handleResult);
    }

    /**
     * 用户撤销纠纷
     * @param id 纠纷ID
     * @return 撤销结果
     */
    @PutMapping("/cancel/{id}")
    public Result cancelDispute(@PathVariable Long id) {
        return orderDisputeService.cancelDispute(id);
    }
}
