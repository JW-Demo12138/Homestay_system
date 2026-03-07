package com.homestay.utils;

/**
 * 响应结果工具类
 * 用于统一API响应格式，支持泛型数据
 * 
 * @param <T> 响应数据类型
 */
public class Result<T> {
    /**
     * 响应状态码
     * 200表示成功，其他表示失败
     */
    private Integer code;
    
    /**
     * 响应消息
     * 描述操作结果的文本信息
     */
    private String message;
    
    /**
     * 响应数据
     * 泛型类型，根据具体接口返回不同类型的数据
     */
    private T data;

    /**
     * 成功响应
     * <p>
     * 返回默认成功消息和数据
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result 成功响应对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应，带自定义消息和数据
     * <p>
     * 返回指定的成功消息和数据
     * 
     * @param message 成功消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result 成功响应对象
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功响应，无数据
     * <p>
     * 返回默认成功消息，无数据
     * 
     * @param <T> 数据类型
     * @return Result 成功响应对象
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 错误响应
     * <p>
     * 返回指定的错误码和错误消息
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result 错误响应对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 错误响应，默认错误码
     * <p>
     * 返回默认错误码(500)和指定的错误消息
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result 错误响应对象
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
    
    /**
     * 获取响应状态码
     * 
     * @return Integer 响应状态码
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 设置响应状态码
     * 
     * @param code 响应状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }
    
    /**
     * 获取响应消息
     * 
     * @return String 响应消息
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置响应消息
     * 
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 获取响应数据
     * 
     * @return T 响应数据
     */
    public T getData() {
        return data;
    }
    
    /**
     * 设置响应数据
     * 
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }
}