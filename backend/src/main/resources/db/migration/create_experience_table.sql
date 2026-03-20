-- 创建乡村特色体验项目表
DROP TABLE IF EXISTS `experience`;
CREATE TABLE `experience` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '体验项目ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '体验名称',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '体验描述',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `duration` int NOT NULL COMMENT '时长（分钟）',
  `available_time` varchar(500) COLLATE utf8mb4_unicode_ci COMMENT '可预约时段',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `longitude` double DEFAULT NULL COMMENT '经度',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '体验地点',
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面图片地址',
  `images` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片地址（多个，逗号分隔）',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '体验类型：民俗手工、农事体验、乡村美食制作等',
  `status` tinyint NOT NULL DEFAULT '2' COMMENT '状态：0-下架，1-上架，2-待审核',
  `owner_id` bigint NOT NULL COMMENT '房东ID',
  `reject_reason` text COLLATE utf8mb4_unicode_ci COMMENT '驳回原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乡村特色体验项目表';