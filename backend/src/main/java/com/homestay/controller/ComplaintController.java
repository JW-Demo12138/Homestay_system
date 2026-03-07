package com.homestay.controller;

import com.homestay.service.ComplaintService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    
    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/list")
    public Result getComplaintList(@RequestParam Map<String, Object> params) {
        return complaintService.getComplaintList(params);
    }

    @PutMapping("/status/{id}")
    public Result updateComplaintStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusData) {
        Integer status = statusData.get("status");
        return complaintService.updateComplaintStatus(id, status);
    }
}
