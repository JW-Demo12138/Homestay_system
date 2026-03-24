package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.entity.Notification;
import com.homestay.mapper.NotificationMapper;
import com.homestay.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Override
    public List<Notification> getListByUserId(Long userId) {
        if (userId == null) {
            return java.util.Collections.emptyList();
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("created_at");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean markAsRead(Long id, Long userId) {
        if (userId == null) {
            return false;
        }
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("user_id", userId);
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
            // 使用字符串形式的SQL来更新字段，手动添加反引号
            return baseMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Notification>()
                    .set("`read`", 1)
                    .eq("user_id", userId)
                    .eq("`read`", 0)) > 0;
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
        queryWrapper.eq("id", id)
                .eq("user_id", userId);
        return baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean create(Notification notification) {
        notification.setRead(0);
        return baseMapper.insert(notification) > 0;
    }
}