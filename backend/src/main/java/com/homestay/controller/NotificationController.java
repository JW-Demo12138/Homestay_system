package com.homestay.controller;

import com.homestay.service.NotificationService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取通知列表
     * @return 通知列表
     */
    @GetMapping("/list")
    public Result<?> getList() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        return Result.success(notificationService.getListByUserId(userId));
    }

    /**
     * 标记通知为已读
     * @param id 通知ID
     * @return 是否成功
     */
    @PutMapping("/read/{id}")
    public Result<?> markAsRead(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        boolean result = notificationService.markAsRead(id, userId);
        return result ? Result.success() : Result.error("操作失败");
    }

    /**
     * 标记所有通知为已读
     * @return 是否成功
     */
    @PutMapping("/read/all")
    public Result<?> markAllAsRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        boolean result = notificationService.markAllAsRead(userId);
        return result ? Result.success() : Result.error("操作失败");
    }

    /**
     * 删除通知
     * @param id 通知ID
     * @return 是否成功
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        boolean result = notificationService.delete(id, userId);
        return result ? Result.success() : Result.error("操作失败");
    }
}