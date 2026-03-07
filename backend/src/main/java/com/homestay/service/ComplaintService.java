package com.homestay.service;

import com.homestay.entity.Complaint;
import com.homestay.utils.Result;

import java.util.Map;

public interface ComplaintService {
    Result getComplaintList(Map<String, Object> params);
    Result updateComplaintStatus(Long id, Integer status);
}
