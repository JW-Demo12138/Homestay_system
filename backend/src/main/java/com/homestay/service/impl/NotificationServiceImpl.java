package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.entity.Notification;
import com.homestay.entity.User;
import com.homestay.mapper.NotificationMapper;
import com.homestay.mapper.UserMapper;
import com.homestay.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Notification> getListByUserId(Long userId) {
        if (userId == null) {
            return java.util.Collections.emptyList();
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_at");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean markAsRead(Long id, Long userId) {
        if (userId == null) {
            return false;
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("user_id", userId);
        Notification notification = baseMapper.selectOne(queryWrapper);
        if (notification != null) {
            notification.setRead(1);
            return baseMapper.updateById(notification) > 0;
        }
        return false;
    }

    @Override
    public boolean markAllAsRead(Long userId) {
        if (userId == null) {
            return false;
        }
        try {
            QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("`read`", 0);
            List<Notification> notifications = baseMapper.selectList(queryWrapper);
            for (Notification notification : notifications) {
                notification.setRead(1);
                baseMapper.updateById(notification);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("标记所有通知为已读失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long id, Long userId) {
        if (userId == null) {
            return false;
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("user_id", userId);
        return baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean create(Notification notification) {
        notification.setRead(0);
        return baseMapper.insert(notification) > 0;
    }

    @Override
    public boolean sendAnnouncement(String title, String message, String announcementType, Long createdBy) {
        try {
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            if ("LANDLORD".equals(announcementType)) {
                userQuery.eq("role", "LANDLORD");
            } else if ("TOURIST".equals(announcementType)) {
                userQuery.eq("role", "TOURIST");
            }
            List<User> users = userMapper.selectList(userQuery);

            // 使用referenceId来记录这批公告的标识（用createdBy和当前时间作为标识）
            Long batchId = System.currentTimeMillis();
            
            for (User user : users) {
                Notification notification = new Notification();
                notification.setUserId(user.getId());
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setType("announcement");
                notification.setReferenceId(batchId);
                notification.setRead(0);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setUpdatedAt(LocalDateTime.now());
                baseMapper.insert(notification);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Notification> getAnnouncementList() {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "announcement").orderByDesc("created_at");
        List<Notification> allAnnouncements = baseMapper.selectList(queryWrapper);
        
        // 去重，只保留每个批次的第一条记录（按referenceId去重
        List<Notification> uniqueAnnouncements = new ArrayList<>();
        List<Long> seenBatchIds = new ArrayList<>();
        
        for (Notification notification : allAnnouncements) {
            Long batchId = notification.getReferenceId();
            if (batchId == null) {
                batchId = notification.getId();
            }
            if (!seenBatchIds.contains(batchId)) {
                seenBatchIds.add(batchId);
                uniqueAnnouncements.add(notification);
            }
        }
        
        return uniqueAnnouncements;
    }

    @Override
    public boolean deleteAnnouncement(Long id) {
        Notification notification = baseMapper.selectById(id);
        if (notification != null && "announcement".equals(notification.getType())) {
            Long batchId = notification.getReferenceId();
            if (batchId == null) {
                batchId = notification.getId();
            }
            
            QueryWrapper<Notification> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("type", "announcement").eq("reference_id", batchId);
            return baseMapper.delete(deleteWrapper) > 0;
        }
        return false;
    }
}