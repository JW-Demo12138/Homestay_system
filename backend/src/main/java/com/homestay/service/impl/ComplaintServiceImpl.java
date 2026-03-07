package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Complaint;
import com.homestay.mapper.ComplaintMapper;
import com.homestay.service.ComplaintService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    
    @Autowired
    private ComplaintMapper complaintMapper;

    @Override
    public Result getComplaintList(Map<String, Object> params) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<Complaint> complaintPage = new Page<>(page, size);
        QueryWrapper<Complaint> queryWrapper = new QueryWrapper<>();

        if (params.get("status") != null) {
            queryWrapper.eq("status", params.get("status"));
        }

        queryWrapper.orderByDesc("create_time");
        IPage<Complaint> result = complaintMapper.selectPage(complaintPage, queryWrapper);
        return Result.success("获取投诉列表成功", result);
    }

    @Override
    public Result updateComplaintStatus(Long id, Integer status) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            return Result.error("投诉不存在");
        }
        complaint.setStatus(status);
        if (status == 1) {
            complaint.setHandleTime(new Date());
        }
        if (complaintMapper.updateById(complaint) > 0) {
            return Result.success("更新投诉状态成功");
        }
        return Result.error("更新投诉状态失败");
    }
}
