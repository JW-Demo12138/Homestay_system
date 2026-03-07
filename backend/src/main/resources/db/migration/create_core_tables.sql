-- 创建用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密）',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TOURIST' COMMENT '角色：LANDLORD-房东，TOURIST-游客，ADMIN-管理员',
  `avatar` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建民宿表
DROP TABLE IF EXISTS `homestay`;
CREATE TABLE `homestay` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '民宿ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '民宿名称',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `longitude` double DEFAULT NULL COMMENT '经度',
  `price` decimal(10,2) NOT NULL COMMENT '价格（每晚）',
  `room_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '房型',
  `facility` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设施',
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面图片地址',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架，2-待审核',
  `owner_id` bigint NOT NULL COMMENT '房东ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  KEY `idx_room_type` (`room_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿表';

-- 创建订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date NOT NULL COMMENT '退房日期',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待支付，PAID-已支付，CANCELLED-已取消，COMPLETED-已完成',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`),
  KEY `idx_check_in_date` (`check_in_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 创建评分表
DROP TABLE IF EXISTS `homestay_rating`;
CREATE TABLE `homestay_rating` (
  `rating_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `rating` tinyint NOT NULL COMMENT '评分：1-5星',
  `comment` text COLLATE utf8mb4_unicode_ci COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rating_id`),
  UNIQUE KEY `uk_homestay_user` (`homestay_id`, `user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分表';

-- 创建投诉表
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投诉内容',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待处理，1-已处理',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投诉表';

-- 创建民宿特色表
DROP TABLE IF EXISTS `homestay_feature`;
CREATE TABLE `homestay_feature` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '特色ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '特色描述',
  `image_url1` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片URL1',
  `image_url2` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片URL2',
  `image_url3` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片URL3',
  `tag` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未审核，1-审核通过，2-审核驳回',
  PRIMARY KEY (`id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿特色表';

-- 创建周边资源表
DROP TABLE IF EXISTS `surrounding_resource`;
CREATE TABLE `surrounding_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源类型',
  `latitude` double NOT NULL COMMENT '纬度',
  `longitude` double NOT NULL COMMENT '经度',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  PRIMARY KEY (`id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='周边资源表';
