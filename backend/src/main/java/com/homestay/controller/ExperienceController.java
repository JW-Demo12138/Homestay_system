package com.homestay.controller;

import com.homestay.entity.Experience;
import com.homestay.service.ExperienceService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 乡村特色体验控制器
 * 处理体验项目相关的HTTP请求，包括列表查询、详情获取、创建、更新、删除、审核等操作
 */
@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    /**
     * 体验服务，处理体验项目相关的业务逻辑
     */
    @Autowired
    private ExperienceService experienceService;

    /**
     * 获取体验项目列表接口
     * @param params 查询参数，包含分页、排序等信息
     * @return 体验项目列表结果，包含体验项目记录和分页信息
     */
    @GetMapping("/list")
    public Result getExperienceList(@RequestParam Map<String, Object> params) {
        // 调用体验服务获取体验项目列表
        return experienceService.getExperienceList(params);
    }

    /**
     * 获取体验项目详情接口
     * @param id 体验项目ID
     * @return 体验项目详情信息
     */
    @GetMapping("/detail/{id}")
    public Result getExperienceDetail(@PathVariable Long id) {
        // 调用体验服务获取体验项目详情
        return experienceService.getExperienceDetail(id);
    }

    /**
     * 创建体验项目接口
     * @param experience 体验项目信息
     * @return 创建结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/create")
    public Result createExperience(@RequestBody Experience experience) {
        // 调用体验服务创建体验项目
        return experienceService.createExperience(experience);
    }

    /**
     * 更新体验项目接口
     * @param id 体验项目ID
     * @param experience 体验项目更新信息
     * @return 更新结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("/update/{id}")
    public Result updateExperience(@PathVariable Long id, @RequestBody Experience experience) {
        // 设置体验项目ID
        experience.setId(id);
        // 调用体验服务更新体验项目
        return experienceService.updateExperience(experience);
    }

    /**
     * 删除体验项目接口
     * @param id 体验项目ID
     * @return 删除结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @DeleteMapping("/delete/{id}")
    public Result deleteExperience(@PathVariable Long id) {
        // 调用体验服务删除体验项目
        return experienceService.deleteExperience(id);
    }

    /**
     * 审核体验项目接口
     * @param reviewData 审核数据，包含体验项目ID、审核状态和备注
     * @return 审核结果
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/review")
    public Result reviewExperience(@RequestBody Map<String, Object> reviewData) {
        Long id = Long.valueOf(reviewData.get("id").toString());
        Integer status = Integer.valueOf(reviewData.get("status").toString());
        String remark = (String) reviewData.get("remark");
        return experienceService.reviewExperience(id, status, remark);
    }

    /**
     * 下架/上架体验项目接口
     * @param id 体验项目ID
     * @param statusData 状态数据，包含状态值
     * @return 操作结果
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @PutMapping("/status/{id}")
    public Result updateExperienceStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusData) {
        Integer status = statusData.get("status");
        return experienceService.updateExperienceStatus(id, status);
    }

    /**
     * 按类型和状态查询体验项目接口
     * @param type 体验类型
     * @param status 状态
     * @return 查询结果
     */
    @GetMapping("/by-type")
    public Result getExperienceByTypeAndStatus(@RequestParam String type, @RequestParam Integer status) {
        return experienceService.getExperienceByTypeAndStatus(type, status);
    }

    /**
     * 获取房东的体验项目列表接口
     * @return 房东的体验项目列表
     */
    @PreAuthorize("hasRole('LANDLORD')")
    @GetMapping("/landlord/list")
    public Result getLandlordExperiences() {
        // 调用体验服务获取房东的体验项目列表
        return experienceService.getLandlordExperiences();
    }

    /**
     * 获取待审核的体验项目列表接口
     * @return 待审核体验项目列表
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public Result getPendingExperiences() {
        return experienceService.getPendingExperiences();
    }
}
