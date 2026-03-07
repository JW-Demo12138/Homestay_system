-- 创建城市表
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '城市ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市名称',
  `province` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省份',
  `latitude` double NOT NULL COMMENT '纬度',
  `longitude` double NOT NULL COMMENT '经度',
  `is_hot` tinyint NOT NULL DEFAULT '1' COMMENT '是否热门城市：1-是，0-否',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_is_hot` (`is_hot`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='城市表';

-- 创建特色标签表
DROP TABLE IF EXISTS `feature_tag`;
CREATE TABLE `feature_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='特色标签表';

-- 创建房间类型表
DROP TABLE IF EXISTS `room_type`;
CREATE TABLE `room_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '房间类型ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '房间类型名称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间类型表';
