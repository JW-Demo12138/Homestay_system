USE homestay;

-- 先删除错误的字段
ALTER TABLE `user` DROP COLUMN `ame`;

-- 重新添加正确的字段
ALTER TABLE `user` ADD `name` VARCHAR(50) DEFAULT NULL COMMENT '姓名' AFTER `phone`;
ALTER TABLE `user` ADD `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱' AFTER `name`;