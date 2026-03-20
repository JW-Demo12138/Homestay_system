-- 扩展订单表，支持体验项目预订
ALTER TABLE `order`
ADD COLUMN `experience_id` bigint DEFAULT NULL COMMENT '体验项目ID',
ADD COLUMN `order_type` varchar(20) NOT NULL DEFAULT 'HOMESTAY' COMMENT '订单类型：HOMESTAY-民宿，EXPERIENCE-体验项目',
ADD COLUMN `experience_date` date DEFAULT NULL COMMENT '体验日期',
ADD COLUMN `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
ADD COLUMN `lock_expire_time` datetime DEFAULT NULL COMMENT '库存锁定过期时间',
ADD COLUMN `experience_time` varchar(50) DEFAULT NULL COMMENT '体验时间段';

-- 添加索引
ALTER TABLE `order`
ADD KEY `idx_experience_id` (`experience_id`),
ADD KEY `idx_order_type` (`order_type`),
ADD KEY `idx_experience_date` (`experience_date`);

-- 添加外键约束
ALTER TABLE `order`
ADD CONSTRAINT `fk_order_experience` FOREIGN KEY (`experience_id`) REFERENCES `experience` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;