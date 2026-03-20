USE homestay;
ALTER TABLE `order` ADD COLUMN `guest_name` VARCHAR(50) DEFAULT NULL COMMENT '顾客姓名' AFTER `pay_time`;
ALTER TABLE `order` ADD COLUMN `guest_phone` VARCHAR(20) DEFAULT NULL COMMENT '顾客电话' AFTER `guest_name`;
ALTER TABLE `order` ADD COLUMN `guest_email` VARCHAR(100) DEFAULT NULL COMMENT '顾客邮箱' AFTER `guest_phone`;