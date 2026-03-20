ALTER TABLE `order`
ADD COLUMN `order_type` varchar(20) NOT NULL DEFAULT 'HOMESTAY',
ADD COLUMN `experience_date` date DEFAULT NULL,
ADD COLUMN `quantity` int NOT NULL DEFAULT 1,
ADD COLUMN `lock_expire_time` datetime DEFAULT NULL,
ADD COLUMN `experience_time` varchar(50) DEFAULT NULL;
