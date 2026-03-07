package com.homestay.controller;

import com.homestay.entity.User;
import com.homestay.service.UserService;
import com.homestay.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 用户控制器
 * 处理用户相关的API请求，包括用户信息管理、密码修改、头像上传和管理员用户管理
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     * <p>
     * 从JWT令牌中获取用户ID，然后查询用户详细信息
     * 
     * @return Result 包含用户信息的响应对象
     */
    @GetMapping("/info")
    public Result getUserInfo() {
        return userService.getUserInfo();
    }

    /**
     * 更新用户信息
     * <p>
     * 更新当前登录用户的基本信息，如姓名、电话、邮箱等
     * 
     * @param user 用户信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    /**
     * 修改密码
     * <p>
     * 验证旧密码后，更新用户密码
     * 
     * @param passwordData 包含旧密码和新密码的Map
     * @return Result 密码修改结果的响应对象
     */
    @PostMapping("/change-password")
    public Result changePassword(@RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        return userService.changePassword(oldPassword, newPassword);
    }

    /**
     * 上传头像
     * <p>
     * 处理用户头像上传，保存文件到本地并更新用户头像信息
     * 
     * @param file 上传的头像文件
     * @return Result 头像上传结果的响应对象
     */
    @PostMapping("/upload-avatar")
    public Result uploadAvatar(@RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
                               @RequestParam(value = "file", required = false) MultipartFile file) {
        MultipartFile uploadFile = avatarFile != null ? avatarFile : file;
        
        if (uploadFile == null || uploadFile.isEmpty()) {
            System.out.println("上传失败：文件为空");
            return Result.error("请选择要上传的文件");
        }
        
        try {
            String originalFilename = uploadFile.getOriginalFilename();
            System.out.println("上传文件名: " + originalFilename);
            System.out.println("文件大小: " + uploadFile.getSize() + " bytes");
            
            if (originalFilename == null || originalFilename.isEmpty()) {
                return Result.error("文件名不能为空");
            }
            
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            System.out.println("文件扩展名: " + fileExtension);
            
            if (!isValidImageExtension(fileExtension)) {
                System.out.println("不支持的文件格式: " + fileExtension);
                return Result.error("只支持jpg、jpeg、png、gif格式的图片");
            }
            
            String fileName = System.currentTimeMillis() + fileExtension;
            
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "avatars" + java.io.File.separator;
            java.io.File uploadPath = new java.io.File(uploadDir);
            
            if (!uploadPath.exists()) {
                boolean created = uploadPath.mkdirs();
                System.out.println("创建上传目录: " + created);
                System.out.println("上传目录路径: " + uploadPath.getAbsolutePath());
            }
            
            java.io.File destFile = new java.io.File(uploadPath, fileName);
            uploadFile.transferTo(destFile);
            System.out.println("文件保存成功: " + destFile.getAbsolutePath());
            
            String avatarUrl = "/uploads/avatars/" + fileName;
            return userService.updateAvatar(avatarUrl);
        } catch (Exception e) {
            System.out.println("头像上传异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证图片扩展名
     * 
     * @param extension 文件扩展名
     * @return boolean 是否有效
     */
    private boolean isValidImageExtension(String extension) {
        return ".jpg".equalsIgnoreCase(extension) 
                || ".jpeg".equalsIgnoreCase(extension) 
                || ".png".equalsIgnoreCase(extension) 
                || ".gif".equalsIgnoreCase(extension);
    }

    /**
     * 获取用户列表（管理员）
     * <p>
     * 管理员获取所有用户列表，支持分页和筛选
     * 
     * @param params 查询参数，包含分页信息和筛选条件
     * @return Result 包含用户列表的响应对象
     */
    @GetMapping("/admin/list")
    public Result getUserList(@RequestParam Map<String, Object> params) {
        return userService.getUserList(params);
    }

    /**
     * 管理员更新用户信息
     * <p>
     * 管理员更新指定用户的信息，包括角色等权限相关字段
     * 
     * @param id 用户ID
     * @param user 用户信息对象，包含要更新的字段
     * @return Result 更新结果的响应对象
     */
    @PutMapping("/admin/update/{id}")
    public Result updateUserByAdmin(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUserByAdmin(user);
    }

    /**
     * 管理员删除用户
     * <p>
     * 管理员删除指定用户
     * 
     * @param id 用户ID
     * @return Result 删除结果的响应对象
     */
    @DeleteMapping("/admin/delete/{id}")
    public Result deleteUserByAdmin(@PathVariable Long id) {
        return userService.deleteUserByAdmin(id);
    }

    @PutMapping("/admin/status/{id}")
    public Result updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusData) {
        Integer status = statusData.get("status");
        return userService.updateUserStatus(id, status);
    }
}
