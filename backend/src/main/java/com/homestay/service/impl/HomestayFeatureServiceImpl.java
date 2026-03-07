package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.HomestayFeature;
import com.homestay.mapper.HomestayFeatureMapper;
import com.homestay.service.HomestayFeatureService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomestayFeatureServiceImpl implements HomestayFeatureService {
    
    @Autowired
    private HomestayFeatureMapper homestayFeatureMapper;

    @Override
    public Result getFeatureList(Map<String, Object> params) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<HomestayFeature> featurePage = new Page<>(page, size);
        QueryWrapper<HomestayFeature> queryWrapper = new QueryWrapper<>();

        if (params.get("status") != null) {
            queryWrapper.eq("status", params.get("status"));
        }

        queryWrapper.orderByDesc("create_time");
        IPage<HomestayFeature> result = homestayFeatureMapper.selectPage(featurePage, queryWrapper);
        return Result.success("获取特色内容列表成功", result);
    }

    @Override
    public Result updateFeatureStatus(Long id, Integer status) {
        HomestayFeature feature = homestayFeatureMapper.selectById(id);
        if (feature == null) {
            return Result.error("特色内容不存在");
        }
        feature.setStatus(status);
        if (homestayFeatureMapper.updateById(feature) > 0) {
            return Result.success("更新特色内容状态成功");
        }
        return Result.error("更新特色内容状态失败");
    }

    /**
     * 构建专题元数据
     * @param tag 专题标签
     * @return 专题元数据
     */
    private Map<String, Object> buildTopicMeta(String tag) {
        Map<String, Object> meta = new HashMap<>();

        switch (tag) {
            case "海景":
                meta.put("title", "海景美宿");
                meta.put("description", "推窗见海，聆听海浪的声音");
                meta.put("bannerImage", "/static/topic/sea.jpg");
                break;

            case "亲子":
                meta.put("title", "亲子优选");
                meta.put("description", "给孩子一个温馨快乐的假期");
                meta.put("bannerImage", "/static/topic/family.jpg");
                break;

            case "网红":
                meta.put("title", "网红打卡");
                meta.put("description", "设计感与高级感并存的空间");
                meta.put("bannerImage", "/static/topic/design.jpg");
                break;

            default:
                meta.put("title", tag + "专题");
                meta.put("description", "精选优质民宿推荐");
                meta.put("bannerImage", "/static/topic/default.jpg");
        }

        return meta;
    }

    @Override
    public Result getFeatureTopic(String tag) {

        List<Map<String, Object>> results = 
                homestayFeatureMapper.selectFeatureTopic(tag);

        if (results.isEmpty()) {
            return Result.error("未找到该专题数据");
        }

        Map<String, Object> topicData = new HashMap<>();

        // 专题元数据
        Map<String, Object> meta = buildTopicMeta(tag);
        topicData.putAll(meta);

        // 构建民宿列表
        List<Map<String, Object>> homestays = new ArrayList<>();

        for (Map<String, Object> row : results) {

            Map<String, Object> item = new HashMap<>();

            item.put("id", row.get("id"));
            item.put("name", row.get("name"));
            item.put("price", row.get("price"));
            item.put("imageUrl", row.get("image_url"));
            item.put("address", row.get("address"));

            homestays.add(item);
        }

        topicData.put("homestays", homestays);

        return Result.success(topicData);
    }
}
