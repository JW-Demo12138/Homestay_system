/*
SQLyog Ultimate v12.08 (32 bit)
MySQL - 8.0.31 : Database - homestay
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`homestay` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `homestay`;

/*Table structure for table `complaint` */

DROP TABLE IF EXISTS `complaint`;

CREATE TABLE `complaint` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投诉内容',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待处理，1-已处理',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_complaint_homestay` FOREIGN KEY (`homestay_id`) REFERENCES `homestay` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_complaint_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投诉表';

/*Data for the table `complaint` */

/*Table structure for table `homestay` */

DROP TABLE IF EXISTS `homestay`;

CREATE TABLE `homestay` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '民宿ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '民宿名称',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `latitude` double NOT NULL COMMENT '纬度',
  `longitude` double NOT NULL COMMENT '经度',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `room_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '房型',
  `facility` text COLLATE utf8mb4_unicode_ci COMMENT '配套设施',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待审核，1-审核通过，2-审核驳回，3-下架',
  `owner_id` bigint NOT NULL COMMENT '房东ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_status` (`status`),
  KEY `idx_address` (`address`),
  KEY `idx_latitude_longitude` (`latitude`,`longitude`),
  CONSTRAINT `fk_homestay_owner` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿表';

/*Data for the table `homestay` */

/*Table structure for table `homestay_feature` */

DROP TABLE IF EXISTS `homestay_feature`;

CREATE TABLE `homestay_feature` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '特色ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '特色描述',
  `image_url1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片1',
  `image_url2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片2',
  `image_url3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片3',
  `tag` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待审核，1-审核通过，2-审核驳回',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`),
  KEY `idx_tag` (`tag`),
  CONSTRAINT `fk_feature_homestay` FOREIGN KEY (`homestay_id`) REFERENCES `homestay` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿特色表';

/*Data for the table `homestay_feature` */

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date NOT NULL COMMENT '退房日期',
  `price` decimal(10,2) NOT NULL COMMENT '订单金额',
  `status` enum('PENDING','PAID','COMPLETED','CANCELLED','REFUNDED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT '状态：待支付/已支付/已完成/已取消/已退款',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_status` (`status`),
  KEY `idx_check_in_date` (`check_in_date`),
  CONSTRAINT `fk_order_homestay` FOREIGN KEY (`homestay_id`) REFERENCES `homestay` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

/*Data for the table `order` */

/*Table structure for table `surrounding_resource` */

DROP TABLE IF EXISTS `surrounding_resource`;

CREATE TABLE `surrounding_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源类型（景点/采摘区等）',
  `latitude` double NOT NULL COMMENT '纬度',
  `longitude` double NOT NULL COMMENT '经度',
  `homestay_id` bigint NOT NULL COMMENT '民宿ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_homestay_id` (`homestay_id`),
  KEY `idx_type` (`type`),
  KEY `idx_latitude_longitude` (`latitude`,`longitude`),
  CONSTRAINT `fk_resource_homestay` FOREIGN KEY (`homestay_id`) REFERENCES `homestay` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='周边资源表';

/*Data for the table `surrounding_resource` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` enum('LANDLORD','TOURIST','ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色：房东/游客/管理员',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`role`,`phone`,`status`,`create_time`,`update_time`) values (1,'admin','$2a$10$eW5eY2VhZGUuY29tLmNvbQ==','ADMIN','13800138000',1,'2026-01-25 06:23:54','2026-01-25 06:23:54'),(2,'ts123456','$2a$10$jO75ImNFNV6JH11qTsznouxNBBGpkjwg9vz7hsOAxyLWsaJdDAjXm','LANDLORD','15912345678',1,'2026-01-29 21:37:23','2026-01-29 21:37:23'),(3,'ts','$2a$10$knuFpeJpo0QN9D9hdZoPa.Xd3kHPQ.BZreHeo6NHePSCyCe6WKLJ2','TOURIST','15912345601',1,'2026-02-02 17:07:18','2026-02-02 17:07:18');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
