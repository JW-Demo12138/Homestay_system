package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Experience;
import com.homestay.entity.Notification;
import com.homestay.mapper.ExperienceMapper;
import com.homestay.service.ExperienceService;
import com.homestay.service.NotificationService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 乡村特色体验服务实现类
 * 实现ExperienceService接口，处理体验项目相关的业务逻辑
 */
@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Autowired
    private ExperienceMapper experienceMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取当前登录用户ID
     * <p>
     * 从Spring Security上下文获取当前登录用户的ID
     * 
     * @return Long 当前登录用户的ID
     */
    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * 获取体验项目列表
     * <p>
     * 支持分页和多条件筛选，包括类型、价格范围等
     * 只返回上架状态的体验项目
     * 
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含体验项目列表的响应对象
     */
    @Override
    public Result getExperienceList(Map<String, Object> params) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<Experience> experiencePage = new Page<>(page, size);
        QueryWrapper<Experience> queryWrapper = new QueryWrapper<>();

        // 只查询上架状态的体验项目
        queryWrapper.eq("status", 1);

        // 添加搜索条件
        if (params.get("keyword") != null) {
            queryWrapper.like("name", params.get("keyword"))
                    .or().like("description", params.get("keyword"))
                    .or().like("location", params.get("keyword"));
        }

        if (params.get("type") != null) {
            queryWrapper.eq("type", params.get("type"));
        }

        if (params.get("minPrice") != null) {
            queryWrapper.ge("price", params.get("minPrice"));
        }

        if (params.get("maxPrice") != null) {
            queryWrapper.le("price", params.get("maxPrice"));
        }

        // 添加民宿ID筛选条件
        if (params.get("homestayId") != null) {
            queryWrapper.eq("homestay_id", params.get("homestayId"));
        }

        IPage<Experience> result = experienceMapper.selectPage(experiencePage, queryWrapper);

        return Result.success("获取体验项目列表成功", result);
    }

    /**
     * 获取体验项目详情
     * <p>
     * 根据体验项目ID查询详细信息
     * 
     * @param id 体验项目ID
     * @return Result 包含体验项目详情的响应对象
     */
    @Override
    public Result getExperienceDetail(Long id) {
        Experience experience = experienceMapper.selectById(id);
        if (experience == null) {
            return Result.error("体验项目不存在");
        }

        return Result.success("获取体验项目详情成功", experience);
    }

    /**
     * 创建体验项目
     * <p>
     * 新增体验项目信息到数据库，设置当前登录用户为房东
     * 
     * @param experience 体验项目信息对象
     * @return Result 创建结果的响应对象
     */
    @Override
    public Result createExperience(Experience experience) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        experience.setOwnerId(currentUserId);
        experience.setStatus(2); // 默认为待审核状态
        
        if (experienceMapper.insert(experience) > 0) {
            return Result.success("创建体验项目成功", experience);
        }
        return Result.error("创建体验项目失败");
    }

    /**
     * 更新体验项目
     * <p>
     * 根据ID更新体验项目信息，确保只能更新自己的体验项目
     * 
     * @param experience 体验项目信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    @Override
    public Result updateExperience(Experience experience) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        Experience existingExperience = experienceMapper.selectById(experience.getId());
        if (existingExperience == null) {
            return Result.error("体验项目不存在");
        }
        
        if (!existingExperience.getOwnerId().equals(currentUserId)) {
            return Result.error("无权更新此体验项目");
        }
        
        // 更新后需要重新审核
        experience.setStatus(2);
        
        if (experienceMapper.updateById(experience) > 0) {
            return Result.success("更新体验项目成功", experience);
        }
        return Result.error("更新体验项目失败");
    }

    /**
     * 删除体验项目
     * <p>
     * 根据ID删除体验项目信息，确保只能删除自己的体验项目
     * 
     * @param id 体验项目ID
     * @return Result 删除结果的响应对象
     */
    @Override
    public Result deleteExperience(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        Experience existingExperience = experienceMapper.selectById(id);
        if (existingExperience == null) {
            return Result.error("体验项目不存在");
        }
        
        if (!existingExperience.getOwnerId().equals(currentUserId)) {
            return Result.error("无权删除此体验项目");
        }
        
        if (experienceMapper.deleteById(id) > 0) {
            return Result.success("删除体验项目成功");
        }
        return Result.error("删除体验项目失败");
    }

    /**
     * 审核体验项目
     * <p>
     * 管理员审核体验项目，设置审核状态
     * 
     * @param id 体验项目ID
     * @param status 审核状态：1-通过，3-驳回
     * @param remark 审核备注
     * @return Result 审核结果的响应对象
     */
    @Override
    public Result reviewExperience(Long id, Integer status, String remark) {
        try {
            Experience experience = experienceMapper.selectById(id);
            if (experience == null) {
                return Result.error("体验项目不存在");
            }
            
            // 验证状态是否合法
            if (status != 1 && status != 3) {
                return Result.error("审核状态无效");
            }
            
            // 更新审核状态
            experience.setStatus(status);
            experience.setRejectReason(status == 3 ? remark : null);
            
            if (experienceMapper.updateById(experience) > 0) {
                // 创建通知给房东
                Notification notification = new Notification();
                notification.setUserId(experience.getOwnerId());
                notification.setType("experience_review");
                notification.setReferenceId(experience.getId());
                
                if (status == 1) {
                    notification.setTitle("体验项目审核通过");
                    notification.setMessage("您的体验项目" + experience.getName() + "已审核通过，可以正常上架。");
                } else if (status == 3) {
                    notification.setTitle("体验项目审核驳回");
                    notification.setMessage("您的体验项目" + experience.getName() + "审核未通过，原因：" + remark);
                }
                
                notificationService.create(notification);
                
                return Result.success("审核操作成功");
            }
            return Result.error("审核操作失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("审核操作失败：" + e.getMessage());
        }
    }

    /**
     * 下架/上架体验项目
     * <p>
     * 房东手动下架或上架体验项目
     * 
     * @param id 体验项目ID
     * @param status 状态：0-下架，1-上架
     * @return Result 操作结果的响应对象
     */
    @Override
    public Result updateExperienceStatus(Long id, Integer status) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        try {
            Experience experience = experienceMapper.selectById(id);
            if (experience == null) {
                return Result.error("体验项目不存在");
            }
            
            // 验证是否是体验项目的房东
            if (!experience.getOwnerId().equals(currentUserId)) {
                return Result.error("无权操作此体验项目");
            }
            
            // 验证状态是否合法
            if (status != 0 && status != 1) {
                return Result.error("状态值无效");
            }
            
            // 更新状态
            experience.setStatus(status);
            
            if (experienceMapper.updateById(experience) > 0) {
                return Result.success("操作成功");
            }
            return Result.error("操作失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 按类型和状态查询体验项目
     * <p>
     * 游客按类型和状态查询体验项目
     * 
     * @param type 体验类型
     * @param status 状态
     * @return Result 包含体验项目列表的响应对象
     */
    @Override
    public Result getExperienceByTypeAndStatus(String type, Integer status) {
        try {
            List<Experience> experiences = experienceMapper.selectByTypeAndStatus(type, status);
            return Result.success("查询体验项目成功", experiences);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询体验项目失败：" + e.getMessage());
        }
    }

    /**
     * 获取房东的体验项目列表
     * <p>
     * 获取当前登录房东的体验项目列表
     * 
     * @return Result 包含房东体验项目列表的响应对象
     */
    @Override
    public Result getLandlordExperiences() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        try {
            List<Experience> experiences = experienceMapper.selectByOwnerId(currentUserId);
            return Result.success("获取房东体验项目成功", experiences);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取房东体验项目失败：" + e.getMessage());
        }
    }

    /**
     * 获取待审核的体验项目列表
     * <p>
     * 管理员获取待审核的体验项目列表
     * 
     * @return Result 包含待审核体验项目列表的响应对象
     */
    @Override
    public Result getPendingExperiences() {
        try {
            List<Experience> experiences = experienceMapper.selectPendingApproval();
            return Result.success("获取待审核体验项目成功", experiences);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取待审核体验项目失败：" + e.getMessage());
        }
    }
}
