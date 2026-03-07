package com.homestay.dto;

import java.util.Date;

/**
 * 订单数据传输对象
 * 用于前端和后端之间的数据传输，确保前端能接收到它期望的数据结构
 */
public class OrderDTO {
    private Long id;
    private String homestayImage;
    private String homestayName;
    private String homestayAddress;
    private String checkInDate;
    private String checkOutDate;
    private Integer guestCount;
    private Double price;
    private String status;
    private Date createTime;

    // getset方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomestayImage() {
        return homestayImage;
    }

    public void setHomestayImage(String homestayImage) {
        this.homestayImage = homestayImage;
    }

    public String getHomestayName() {
        return homestayName;
    }

    public void setHomestayName(String homestayName) {
        this.homestayName = homestayName;
    }

    public String getHomestayAddress() {
        return homestayAddress;
    }

    public void setHomestayAddress(String homestayAddress) {
        this.homestayAddress = homestayAddress;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
