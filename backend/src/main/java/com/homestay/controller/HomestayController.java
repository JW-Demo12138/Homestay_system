package com.homestay.controller;

import com.homestay.entity.Homestay;
import com.homestay.service.HomestayService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 民宿控制器
 * 处理民宿相关的HTTP请求，包括列表查询、详情获取、创建、更新、删除、搜索、推荐等操作
 */
@RestController
@RequestMapping("/api/homestay")
public class HomestayController {

    /**
     * 民宿服务，处理民宿相关的业务逻辑
     */
    @Autowired
    private HomestayService homestayService;

    /**
     * 获取民宿列表接口
     * @param params 查询参数，包含分页、排序等信息
     * @return 民宿列表结果，包含民宿记录和分页信息
     */
    @GetMapping("/list")
    public Result getHomestayList(@RequestParam Map<String, Object> params) {
        // 调用民宿服务获取民宿列表
        return homestayService.getHomestayList(params);
    }

    /**
     * 获取民宿详情接口
     * @param id 民宿ID
     * @return 民宿详情信息
     */
    @GetMapping("/detail/{id}")
    public Result getHomestayDetail(@PathVariable Long id) {
        // 调用民宿服务获取民宿详情
        return homestayService.getHomestayDetail(id);
    }

    /**
     * 创建民宿接口
     * @param homestay 民宿信息
     * @return 创建结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/create")
    public Result createHomestay(@RequestBody Homestay homestay) {
        // 调用民宿服务创建民宿
        return homestayService.createHomestay(homestay);
    }

    /**
     * 更新民宿接口
     * @param id 民宿ID
     * @param homestay 民宿更新信息
     * @return 更新结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("/update/{id}")
    public Result updateHomestay(@PathVariable Long id, @RequestBody Homestay homestay) {
        // 设置民宿ID
        homestay.setId(id);
        // 调用民宿服务更新民宿
        return homestayService.updateHomestay(homestay);
    }

    /**
     * 删除民宿接口
     * @param id 民宿ID
     * @return 删除结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping("/delete/{id}")
    public Result deleteHomestay(@PathVariable Long id) {
        // 调用民宿服务删除民宿
        return homestayService.deleteHomestay(id);
    }

    /**
     * 搜索民宿接口
     * @param params 搜索参数，包含关键词、价格范围、设施等
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Result searchHomestay(@RequestParam Map<String, Object> params) {
        // 调用民宿服务搜索民宿
        return homestayService.searchHomestay(params);
    }

    /**
     * 获取推荐民宿接口
     * @return 推荐民宿列表
     */
    @GetMapping("/recommend")
    public Result getRecommendedHomestays() {
        // 调用民宿服务获取推荐民宿
        return homestayService.getRecommendedHomestays();
    }

    /**
     * 获取房东的民宿列表接口
     * @return 房东的民宿列表
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping("/landlord/list")
    public Result getLandlordHomestays() {
        // 调用民宿服务获取房东的民宿列表
        return homestayService.getLandlordHomestays();
    }

    /**
     * 批量导入民宿接口
     * @param homestays 民宿信息列表
     * @return 导入结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/import")
    public Result importHomestays(@RequestBody java.util.List<com.homestay.entity.Homestay> homestays) {
        // 调用民宿服务批量导入民宿
        return homestayService.importHomestays(homestays);
    }
    
    /**
     * 审核民宿接口
     * @param reviewData 审核数据，包含民宿ID、审核状态和备注
     * @return 审核结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/review")
    public Result reviewHomestay(@RequestBody Map<String, Object> reviewData) {
        Long id = Long.valueOf(reviewData.get("id").toString());
        Integer status = Integer.valueOf(reviewData.get("status").toString());
        String remark = (String) reviewData.get("remark");
        return homestayService.reviewHomestay(id, status, remark);
    }
    
    /**
     * 下架/上架民宿接口
     * @param id 民宿ID
     * @param statusData 状态数据，包含状态值
     * @return 操作结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("/status/{id}")
    public Result updateHomestayStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusData) {
        Integer status = statusData.get("status");
        return homestayService.updateHomestayStatus(id, status);
    }
    
    /**
     * 按名称和状态查询民宿接口
     * @param params 查询参数，包含名称关键词和状态
     * @return 查询结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping("/query")
    public Result queryHomestayByNameAndStatus(@RequestParam Map<String, Object> params) {
        return homestayService.queryHomestayByNameAndStatus(params);
    }
    
    /**
     * 获取待审核的民宿列表接口
     * @return 待审核民宿列表
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public Result getPendingHomestays() {
        return homestayService.getPendingHomestays();
    }
}