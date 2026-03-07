USE homestay;

DROP TABLE IF EXISTS `homestay_rating`;
CREATE TABLE `homestay_rating` (
  `rating_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `rating` tinyint NOT NULL COMMENT '评分：1-5星',
  `comment` text COLLATE utf8mb4_unicode_ci COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rating_id`),
  UNIQUE KEY `uk_homestay_user` (`homestay_id`, `user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分表';