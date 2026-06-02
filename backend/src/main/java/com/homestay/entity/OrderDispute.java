package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单纠纷实体类
 */
@Data
@TableName("order_dispute")
public class OrderDispute {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long userId;

    private Long homestayId;

    private String disputeType;

    private String disputeTitle;

    private String disputeContent;

    private String evidenceImages;

    private String status;

    private String handleResult;

    private LocalDateTime handleTime;

    private Long handleUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
