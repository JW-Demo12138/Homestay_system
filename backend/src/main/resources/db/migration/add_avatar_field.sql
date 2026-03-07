-- 添加用户头像字段
-- 为 user 表添加 avatar 字段，用于存储用户头像URL

ALTER TABLE `user` ADD COLUMN `avatar` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL' AFTER `role`;
