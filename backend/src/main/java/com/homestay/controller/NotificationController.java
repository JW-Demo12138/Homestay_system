package com.homestay.controller;

import com.homestay.service.NotificationService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            notificationService.markAllAsRead(userId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("标记所有通知为已读失败: " + e.getMessage());
            return Result.error("操作失败: " + e.getMessage());
        }
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

    /**
     * 管理员：发送公告
     * @param data 公告数据
     * @return 是否成功
     */
    @PostMapping("/admin/send")
    public Result<?> sendAnnouncement(@RequestBody Map<String, String> data) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            String title = data.get("title");
            String message = data.get("message");
            String announcementType = data.get("announcementType");
            
            if (title == null || title.isEmpty() || message == null || message.isEmpty()) {
                return Result.error("标题和内容不能为空");
            }
            if (announcementType == null) {
                announcementType = "ALL";
            }
            
            boolean result = notificationService.sendAnnouncement(title, message, announcementType, userId);
            return result ? Result.success("公告发送成功") : Result.error("公告发送失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("发送公告失败: " + e.getMessage());
        }
    }

    /**
     * 管理员：获取公告列表
     * @return 公告列表
     */
    @GetMapping("/admin/list")
    public Result<?> getAnnouncementList() {
        return Result.success(notificationService.getAnnouncementList());
    }

    /**
     * 管理员：删除公告
     * @param id 公告ID
     * @return 是否成功
     */
    @DeleteMapping("/admin/delete/{id}")
    public Result<?> deleteAnnouncement(@PathVariable Long id) {
        boolean result = notificationService.deleteAnnouncement(id);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }
}