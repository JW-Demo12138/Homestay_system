-- 更新通知表，添加公告功能字段
ALTER TABLE `notification` 
ADD COLUMN `is_announcement` TINYINT(1) DEFAULT 0 COMMENT '是否是公告：0-普通通知，1-系统公告',
ADD COLUMN `announcement_type` VARCHAR(50) DEFAULT NULL COMMENT '公告类型：ALL-全体，LANDLORD-房东，TOURIST-游客',
ADD COLUMN `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID';
