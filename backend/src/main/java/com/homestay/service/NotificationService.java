package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.entity.Notification;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    /**
     * 获取用户的通知列表
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getListByUserId(Long userId);

    /**
     * 标记通知为已读
     * @param id 通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long id, Long userId);

    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAllAsRead(Long userId);

    /**
     * 删除通知
     * @param id 通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean delete(Long id, Long userId);

    /**
     * 创建通知
     * @param notification 通知对象
     * @return 是否成功
     */
    boolean create(Notification notification);

    /**
     * 发送公告
     * @param title 公告标题
     * @param message 公告内容
     * @param announcementType 公告类型：ALL-全体，LANDLORD-房东，TOURIST-游客
     * @param createdBy 创建人ID
     * @return 是否成功
     */
    boolean sendAnnouncement(String title, String message, String announcementType, Long createdBy);

    /**
     * 获取公告列表（管理员）
     * @return 公告列表
     */
    List<Notification> getAnnouncementList();

    /**
     * 删除公告
     * @param id 公告ID
     * @return 是否成功
     */
    boolean deleteAnnouncement(Long id);
}