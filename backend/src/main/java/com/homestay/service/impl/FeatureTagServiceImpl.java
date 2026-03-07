package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homestay.entity.FeatureTag;
import com.homestay.entity.RoomType;
import com.homestay.mapper.FeatureTagMapper;
import com.homestay.mapper.RoomTypeMapper;
import com.homestay.service.FeatureTagService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureTagServiceImpl implements FeatureTagService {
    
    @Autowired
    private FeatureTagMapper featureTagMapper;
    
    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Override
    public Result getAllFeatureTags() {
        QueryWrapper<FeatureTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order");
        List<FeatureTag> tags = featureTagMapper.selectList(queryWrapper);
        return Result.success(tags);
    }

    @Override
    public Result getAllRoomTypes() {
        QueryWrapper<RoomType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order");
        List<RoomType> roomTypes = roomTypeMapper.selectList(queryWrapper);
        return Result.success(roomTypes);
    }
}
