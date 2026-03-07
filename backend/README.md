# 民宿预订系统后端

## 项目介绍
基于Spring Boot 2.7.17和MyBatis Plus 3.5.3.1开发的民宿预订系统后端。

## 技术栈
- Spring Boot 2.7.17
- MyBatis Plus 3.5.3.1
- MySQL 8.0
- JWT

## 运行说明

### 1. 环境要求
- JDK 1.8
- MySQL 8.0
- IDEA（推荐）

### 2. 数据库配置
1. 创建名为`homestay`的数据库
2. 数据库用户名：root
3. 数据库密码：123456
4. 数据库地址：localhost:3306

### 3. 使用IDEA运行项目
1. 打开IDEA，导入项目
2. 等待IDEA自动下载依赖（可能需要几分钟）
3. 右键点击`HomestayApplication.java`文件
4. 选择`Run 'HomestayApplication'`或`Debug 'HomestayApplication'`

### 4. 项目结构
```
src/main/java/com/homestay/
├── entity/          # 实体类
├── mapper/          # Mapper接口
├── utils/           # 工具类
└── HomestayApplication.java  # 启动类
```

## 注意事项
1. 首次运行时，IDEA会自动下载所有依赖，请确保网络连接正常
2. 若遇到依赖下载缓慢，可以配置阿里云镜像
3. 项目默认端口为8080

## 常见问题

### 问题1：Lombok相关错误
**解决方法**：项目已移除Lombok依赖，所有实体类均使用手动getter和setter方法，无需安装Lombok插件。

### 问题2：MyBatis Plus相关错误
**解决方法**：已配置@MapperScan注解，确保所有Mapper接口都在com.homestay.mapper包下。

### 问题3：数据库连接错误
**解决方法**：检查application.yml中的数据库配置是否正确，确保MySQL服务正在运行。
