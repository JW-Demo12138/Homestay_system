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
        Notification notification = new Notification();
        notification.setRead(1);
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("read", 0);
        return baseMapper.update(notification, queryWrapper) > 0;
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