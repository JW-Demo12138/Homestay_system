-- ========================================
-- 民宿管理系统 - 管理员功能表
-- ========================================

-- 系统配置表
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text COLLATE utf8mb4_unicode_ci COMMENT '配置值',
  `config_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STRING' COMMENT '配置类型：STRING-字符串，NUMBER-数字，BOOLEAN-布尔值',
  `config_group` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'GENERAL' COMMENT '配置分组：GENERAL-通用配置，PAYMENT-支付配置，NOTIFICATION-通知配置',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_config_group` (`config_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 初始化系统配置数据（毕业设计模拟配置）
INSERT INTO `system_config` (`config_key`, `config_value`, `config_type`, `config_group`, `description`) VALUES
('payment.enabled', 'true', 'BOOLEAN', 'PAYMENT', '是否启用支付功能'),
('payment.mock.mode', 'true', 'BOOLEAN', 'PAYMENT', '是否启用模拟支付模式'),
('payment.mock.delay', '2000', 'NUMBER', 'PAYMENT', '模拟支付延迟时间（毫秒）'),
('payment.default.method', 'alipay', 'STRING', 'PAYMENT', '默认支付方式：alipay-支付宝，wechat-微信支付'),
('notification.sms.enabled', 'false', 'BOOLEAN', 'NOTIFICATION', '是否启用短信通知'),
('notification.email.enabled', 'false', 'BOOLEAN', 'NOTIFICATION', '是否启用邮件通知'),
('notification.template.register', '您的验证码是：{code}，请在5分钟内完成验证。', 'STRING', 'NOTIFICATION', '注册验证码模板'),
('notification.template.order.created', '您的订单{orderNo}已创建成功，请及时支付。', 'STRING', 'NOTIFICATION', '订单创建通知模板'),
('notification.template.order.paid', '您的订单{orderNo}已支付成功，入住愉快！', 'STRING', 'NOTIFICATION', '订单支付成功通知模板'),
('general.max.file.size', '10', 'NUMBER', 'GENERAL', '最大文件上传大小（MB）'),
('general.order.expire.time', '30', 'NUMBER', 'GENERAL', '订单过期时间（分钟）'),
('general.site.name', '乡村民宿预订平台', 'STRING', 'GENERAL', '网站名称');

-- ========================================
-- 订单纠纷表
DROP TABLE IF EXISTS `order_dispute`;
CREATE TABLE `order_dispute` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '纠纷ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `dispute_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '纠纷类型：QUALITY-质量问题，SERVICE-服务问题，PRICE-价格问题，OTHER-其他问题',
  `dispute_title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '纠纷标题',
  `dispute_content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '纠纷内容',
  `evidence_images` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证据图片URL列表，逗号分隔',
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT '纠纷状态：PENDING-待处理，PROCESSING-处理中，RESOLVED-已解决，CLOSED-已关闭',
  `handle_result` text COLLATE utf8mb4_unicode_ci COMMENT '处理结果',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_user_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单纠纷表';
