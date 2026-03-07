package com.homestay.service;

import com.homestay.entity.HomestayFeature;
import com.homestay.utils.Result;

import java.util.Map;

public interface HomestayFeatureService {
    Result getFeatureList(Map<String, Object> params);
    Result updateFeatureStatus(Long id, Integer status);
    Result getFeatureTopic(String tag);
}
