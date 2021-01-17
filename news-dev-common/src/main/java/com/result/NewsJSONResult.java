package com.result;

import com.enums.ResponseStatusEnum;

import java.util.Map;

/**
 * 自定义响应数据工具类
 * <p>
 * Created by TongHaiJun
 * 2021/1/15 14:45
 */
public class NewsJSONResult {

    // 响应业务状态码
    private Integer status;

    // 响应消息
    private String msg;

    // 是否成功
    private Boolean success;

    // 响应数据（可以是Object,也可以是List或Map等）
    private Object data;

    /**
     * 成功返回
     * 不带有数据的 直接调用success方法,data无须传入（其实就是null）
     */
    public static NewsJSONResult success() {
        return new NewsJSONResult(ResponseStatusEnum.SUCCESS);
    }

    /**
     * 成功返回
     * 带有数据的 直接往success方法丢data数据即可
     */
    public static NewsJSONResult success(Object data) {
        return new NewsJSONResult(data);
    }

    /**
     * 错误返回
     * 直接调用error方法即可 当然也可以在ResponseStatusEnum中自定义错误后再返回也都可以
     */
    public static NewsJSONResult error() {
        return new NewsJSONResult(ResponseStatusEnum.FAILED);
    }

    /**
     * 错误返回
     * 直接返回错误的消息
     */
    public static NewsJSONResult errorMsg(String msg) {
        return new NewsJSONResult(ResponseStatusEnum.FAILED, msg);
    }

    /**
     * 错误返回
     * map中包含了多条错误信息 可以用于表单验证 把错误统一的全部返回出去
     */
    public static NewsJSONResult errorMap(Map map) {
        return new NewsJSONResult(ResponseStatusEnum.FAILED, map);
    }

    /**
     * 错误返回
     * token异常 一些通用的可以在这里统一定义
     */
    public static NewsJSONResult errorTicket() {
        return new NewsJSONResult(ResponseStatusEnum.TICKET_INVALID);
    }

    /**
     * 自定义错误范围
     * 需要传入一个自定义的枚举 可以到[ResponseStatusEnum.java]中自定义后再传入
     */
    public static NewsJSONResult errorCustom(ResponseStatusEnum responseStatus) {
        return new NewsJSONResult(responseStatus);
    }

    public static NewsJSONResult exception(ResponseStatusEnum responseStatus) {
        return new NewsJSONResult(responseStatus);
    }

    public NewsJSONResult() {

    }

    private NewsJSONResult(Object data) {
        this.status = ResponseStatusEnum.SUCCESS.status();
        this.msg = ResponseStatusEnum.SUCCESS.msg();
        this.success = ResponseStatusEnum.SUCCESS.success();
        this.data = data;
    }

    private NewsJSONResult(ResponseStatusEnum responseStatus) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
    }

    private NewsJSONResult(ResponseStatusEnum responseStatus, Object data) {
        this.status = responseStatus.status();
        this.msg = responseStatus.msg();
        this.success = responseStatus.success();
        this.data = data;
    }

    private NewsJSONResult(ResponseStatusEnum responseStatus, String msg) {
        this.status = responseStatus.status();
        this.msg = msg;
        this.success = responseStatus.success();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
