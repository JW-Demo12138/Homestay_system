-- 为homestay表添加image_url字段
ALTER TABLE homestay ADD COLUMN image_url VARCHAR(500) COMMENT '民宿封面图片地址' AFTER facility;
