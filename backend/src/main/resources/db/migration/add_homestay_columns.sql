-- 添加民宿表缺失的列
ALTER TABLE `homestay`
ADD COLUMN `view_count` int DEFAULT 0 COMMENT '浏览量',
ADD COLUMN `recommend_weight` int DEFAULT 0 COMMENT '推荐权重',
ADD COLUMN `avg_rating` decimal(3,2) DEFAULT 0.00 COMMENT '平均评分',
ADD COLUMN `reject_reason` varchar(500) DEFAULT NULL COMMENT '驳回原因';
