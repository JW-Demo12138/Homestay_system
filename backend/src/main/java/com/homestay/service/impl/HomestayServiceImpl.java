package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Homestay;
import com.homestay.entity.Notification;
import com.homestay.entity.User;
import com.homestay.mapper.HomestayMapper;
import com.homestay.mapper.UserMapper;
import com.homestay.service.HomestayService;
import com.homestay.service.NotificationService;
import com.homestay.utils.Result;
import com.homestay.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 民宿服务实现类
 * 实现HomestayService接口，处理民宿相关的业务逻辑
 */
@Service
public class HomestayServiceImpl implements HomestayService {

    @Autowired
    private HomestayMapper homestayMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取民宿列表
     * <p>
     * 支持分页和多条件筛选，包括关键词搜索、价格范围、标签等
     * 只返回上架状态的民宿
     * 
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含民宿列表的响应对象
     */
    @Override
    public Result getHomestayList(Map<String, Object> params) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;

        IPage<Homestay> homestayPage = new Page<>(page, size);
        QueryWrapper<Homestay> queryWrapper = new QueryWrapper<>();

        // 只查询上架状态的民宿
        queryWrapper.eq("status", 1);

        // 添加搜索条件
        if (params.get("keyword") != null) {
            queryWrapper.like("name", params.get("keyword"))
                    .or().like("address", params.get("keyword"))
                    .or().like("description", params.get("keyword"));
        }

        if (params.get("minPrice") != null) {
            queryWrapper.ge("price", params.get("minPrice"));
        }

        if (params.get("maxPrice") != null) {
            queryWrapper.le("price", params.get("maxPrice"));
        }

        IPage<Homestay> result = homestayMapper.selectPage(homestayPage, queryWrapper);

        return Result.success("获取民宿列表成功", result);
    }

    /**
     * 获取民宿详情
     * <p>
     * 根据民宿ID查询民宿详细信息，包括房东信息
     * 
     * @param id 民宿ID
     * @return Result 包含民宿详情的响应对象
     */
    @Override
    public Result getHomestayDetail(Long id) {
        Homestay homestay = homestayMapper.selectById(id);
        if (homestay == null) {
            return Result.error("民宿不存在");
        }

        if (homestay.getOwnerId() != null) {
            User landlord = userMapper.selectById(homestay.getOwnerId());
            if (landlord != null) {
                homestay.setLandlordUsername(landlord.getUsername());
                homestay.setLandlordPhone(landlord.getPhone());
            }
        }

        // 浏览量自动 +1
        if (homestay.getViewCount() == null) {
            homestay.setViewCount(0);
        }
        homestay.setViewCount(homestay.getViewCount() + 1);
        homestayMapper.updateById(homestay);

        return Result.success("获取民宿详情成功", homestay);
    }

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
     * 创建民宿
     * <p>
     * 新增民宿信息到数据库，设置当前登录用户为房东
     * 
     * @param homestay 民宿信息对象
     * @return Result 创建结果的响应对象
     */
    @Override
    public Result createHomestay(Homestay homestay) {
        Long currentUserId = getCurrentUserId();
        System.out.println("========== 创建民宿调试信息 ==========");
        System.out.println("当前登录用户ID: " + currentUserId);
        if (currentUserId == null) {
            System.out.println("用户未登录或登录已过期");
            return Result.error("用户未登录或登录已过期");
        }
        
        homestay.setOwnerId(currentUserId);
        System.out.println("设置民宿的房东ID为: " + currentUserId);
        
        // 所有新创建的民宿都设置为待审核状态
        homestay.setStatus(2);
        
        // 新增初始化
        homestay.setViewCount(0);
        homestay.setRecommendWeight(0);
        homestay.setAvgRating(0.0);
        
        if (homestayMapper.insert(homestay) > 0) {
            System.out.println("民宿创建成功, ID: " + homestay.getId() + ", 房东ID: " + homestay.getOwnerId());
            System.out.println("========================================");
            return Result.success("创建民宿成功", homestay);
        }
        System.out.println("民宿创建失败");
        System.out.println("========================================");
        return Result.error("创建民宿失败");
    }

    /**
     * 更新民宿
     * <p>
     * 根据ID更新民宿信息，确保只能更新自己的民宿
     * 
     * @param homestay 民宿信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    @Override
    public Result updateHomestay(Homestay homestay) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        Homestay existingHomestay = homestayMapper.selectById(homestay.getId());
        if (existingHomestay == null) {
            return Result.error("民宿不存在");
        }
        
        if (!existingHomestay.getOwnerId().equals(currentUserId)) {
            return Result.error("无权更新此民宿");
        }
        
        // 如果民宿当前不是待审核状态，修改后设置为待审核状态
        if (homestay.getStatus() != 2) {
            homestay.setStatus(2);
        }
        
        if (homestayMapper.updateById(homestay) > 0) {
            return Result.success("更新民宿成功，等待管理员审核", homestay);
        }
        return Result.error("更新民宿失败");
    }

    /**
     * 删除民宿
     * <p>
     * 根据ID删除民宿信息，确保只能删除自己的民宿
     * 
     * @param id 民宿ID
     * @return Result 删除结果的响应对象
     */
    @Override
    public Result deleteHomestay(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        Homestay existingHomestay = homestayMapper.selectById(id);
        if (existingHomestay == null) {
            return Result.error("民宿不存在");
        }
        
        if (!existingHomestay.getOwnerId().equals(currentUserId)) {
            return Result.error("无权删除此民宿");
        }
        
        if (homestayMapper.deleteById(id) > 0) {
            return Result.success("删除民宿成功");
        }
        return Result.error("删除民宿失败");
    }

    /**
     * 搜索民宿
     * <p>
     * 调用getHomestayList方法实现搜索功能
     * 
     * @param params 搜索参数，包含关键词、价格范围等
     * @return Result 包含搜索结果的响应对象
     */
    @Override
    public Result searchHomestay(Map<String, Object> params) {
        return getHomestayList(params);
    }

    /**
     * 获取推荐民宿
     * <p>
     * 获取前10个上架的推荐民宿，按创建时间倒序排列
     * 
     * @return Result 包含推荐民宿列表的响应对象
     */
    @Override
    public Result getRecommendedHomestays() {
        QueryWrapper<Homestay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT 10");
        return Result.success("获取推荐民宿成功", homestayMapper.selectList(queryWrapper));
    }

    /**
     * 获取房东民宿
     * <p>
     * 获取当前登录房东的民宿列表
     * 
     * @return Result 包含房东民宿列表的响应对象
     */
    @Override
    public Result getLandlordHomestays() {
        Long currentUserId = getCurrentUserId();
        System.out.println("========== 调试信息 ==========");
        System.out.println("当前登录用户ID: " + currentUserId);
        if (currentUserId == null) {
            System.out.println("用户未登录或登录已过期");
            return Result.error("用户未登录或登录已过期");
        }
        
        QueryWrapper<Homestay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", currentUserId);
        List<Homestay> homestays = homestayMapper.selectList(queryWrapper);
        System.out.println("查询到的民宿数量: " + homestays.size());
        for (Homestay h : homestays) {
            System.out.println("  - 民宿ID: " + h.getId() + ", 名称: " + h.getName() + ", 房东ID: " + h.getOwnerId());
        }
        System.out.println("===============================");
        return Result.success("获取房东民宿成功", homestays);
    }

    /**
     * 批量导入民宿
     * <p>
     * 批量将民宿信息导入数据库
     * 
     * @param homestays 民宿信息列表
     * @return Result 导入结果的响应对象
     */
    @Override
    public Result importHomestays(java.util.List<Homestay> homestays) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        try {
            int count = 0;
            for (Homestay homestay : homestays) {
                if (homestay.getStatus() == null) {
                    homestay.setStatus(2); // 默认为待审核状态
                }
                if (homestay.getOwnerId() == null) {
                    homestay.setOwnerId(currentUserId);
                }
                if (homestay.getRoomType() == null) {
                    homestay.setRoomType("标准间");
                }
                if (homestay.getFacility() == null) {
                    homestay.setFacility("WiFi,空调,热水");
                }
                
                if (homestayMapper.insert(homestay) > 0) {
                    count++;
                }
            }
            return Result.success("导入民宿成功，共导入 " + count + " 条记录", count);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("导入民宿失败：" + e.getMessage());
        }
    }
    
    /**
     * 审核民宿
     * <p>
     * 管理员审核民宿，设置审核状态
     * 
     * @param id 民宿ID
     * @param status 审核状态：1-通过，3-驳回
     * @param remark 审核备注
     * @return Result 审核结果的响应对象
     */
    @Override
    public Result reviewHomestay(Long id, Integer status, String remark) {
        try {
            Homestay homestay = homestayMapper.selectById(id);
            if (homestay == null) {
                return Result.error("民宿不存在");
            }
            
            // 验证状态是否合法
            if (status != 1 && status != 3) {
                return Result.error("审核状态无效");
            }
            
            // 更新审核状态
            homestay.setStatus(status);
            homestay.setRejectReason(status == 3 ? remark : null);
            
            if (homestayMapper.updateById(homestay) > 0) {
                // 创建通知给房东
                Notification notification = new Notification();
                notification.setUserId(homestay.getOwnerId());
                notification.setType("homestay_review");
                notification.setReferenceId(homestay.getId());
                
                if (status == 1) {
                    notification.setTitle("民宿审核通过");
                    notification.setMessage("您的民宿" + homestay.getName() + "已审核通过，可以正常上架。");
                } else if (status == 3) {
                    notification.setTitle("民宿审核驳回");
                    notification.setMessage("您的民宿" + homestay.getName() + "审核未通过，原因：" + remark);
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
     * 下架/上架民宿
     * <p>
     * 房东手动下架或上架民宿
     * 
     * @param id 民宿ID
     * @param status 状态：0-下架，1-上架
     * @return Result 操作结果的响应对象
     */
    @Override
    public Result updateHomestayStatus(Long id, Integer status) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        try {
            Homestay homestay = homestayMapper.selectById(id);
            if (homestay == null) {
                return Result.error("民宿不存在");
            }
            
            // 验证是否是民宿的房东
            if (!homestay.getOwnerId().equals(currentUserId)) {
                return Result.error("无权操作此民宿");
            }
            
            // 验证状态是否合法
            if (status != 0 && status != 1) {
                return Result.error("状态值无效");
            }
            
            // 更新状态
            homestay.setStatus(status);
            
            if (homestayMapper.updateById(homestay) > 0) {
                return Result.success("操作成功");
            }
            return Result.error("操作失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败：" + e.getMessage());
        }
    }
    
    /**
     * 按名称和状态查询民宿
     * <p>
     * 房东按名称和状态查询自己的民宿
     * 
     * @param params 查询参数，包含名称关键词和状态
     * @return Result 包含民宿列表的响应对象
     */
    @Override
    public Result queryHomestayByNameAndStatus(Map<String, Object> params) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户未登录或登录已过期");
        }
        
        try {
            QueryWrapper<Homestay> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("owner_id", currentUserId);
            
            // 添加名称关键词搜索
            if (params.get("name") != null) {
                queryWrapper.like("name", params.get("name"));
            }
            
            // 添加状态筛选
            if (params.get("status") != null) {
                queryWrapper.eq("status", params.get("status"));
            }
            
            List<Homestay> homestays = homestayMapper.selectList(queryWrapper);
            return Result.success("查询民宿成功", homestays);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询民宿失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取待审核的民宿列表
     * <p>
     * 管理员获取待审核的民宿列表
     * 
     * @return Result 包含待审核民宿列表的响应对象
     */
    @Override
    public Result getPendingHomestays() {
        try {
            QueryWrapper<Homestay> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 2); // 2-待审核
            queryWrapper.orderByDesc("create_time");
            
            List<Homestay> homestays = homestayMapper.selectList(queryWrapper);
            return Result.success("获取待审核民宿成功", homestays);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取待审核民宿失败：" + e.getMessage());
        }
    }
}
