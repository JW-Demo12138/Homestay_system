-- 创建系统配置表
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键（唯一）',
  `config_value` text COLLATE utf8mb4_unicode_ci COMMENT '配置值',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入初始配置数据
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('order.payment.timeout', '30', '订单支付超时时间（分钟）'),
('order.auto.confirm.days', '7', '订单自动确认收货天数'),
('platform.fee.rate', '5', '平台手续费率（%）'),
('notify.template.order.created', '您的订单已创建，订单号：{orderNo}', '订单创建通知模板'),
('notify.template.order.paid', '订单支付成功，感谢您的预订！', '订单支付成功通知模板'),
('notify.template.order.cancelled', '订单已取消，如有疑问请联系客服', '订单取消通知模板'),
('notify.template.review.approved', '您的{type}审核已通过！', '审核通过通知模板'),
('notify.template.review.rejected', '您的{type}审核未通过，原因：{reason}', '审核驳回通知模板');
