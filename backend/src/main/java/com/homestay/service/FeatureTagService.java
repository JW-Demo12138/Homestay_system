package com.homestay.service;

import com.homestay.entity.FeatureTag;
import com.homestay.entity.RoomType;
import com.homestay.utils.Result;

import java.util.List;

public interface FeatureTagService {
    Result getAllFeatureTags();
    Result getAllRoomTypes();
}
