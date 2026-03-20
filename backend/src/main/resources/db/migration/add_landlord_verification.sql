-- 扩展用户表，添加房东认证相关字段
ALTER TABLE `user`
ADD COLUMN `id_card` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号',
ADD COLUMN `id_card_photo` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证照片URL',
ADD COLUMN `business_license` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '营业执照照片URL',
ADD COLUMN `homestay_license` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '民宿经营许可证照片URL',
ADD COLUMN `verification_status` tinyint NOT NULL DEFAULT '0' COMMENT '认证状态：0-未提交，1-审核中，2-审核通过，3-审核驳回',
ADD COLUMN `verification_remark` text COLLATE utf8mb4_unicode_ci COMMENT '审核备注',
ADD COLUMN `verification_time` datetime DEFAULT NULL COMMENT '审核时间';

-- 添加索引
ALTER TABLE `user`
ADD KEY `idx_verification_status` (`verification_status`);