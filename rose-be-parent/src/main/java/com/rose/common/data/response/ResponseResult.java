package com.rose.common.data.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 功能：返回实体类
 * @author sunpeng
 * @date 2017
 */
@Setter
@Getter
@ToString
public class ResponseResult implements Serializable {

    // 响应状态
    private Integer code;

    // 响应消息
    private String msg;

    // 响应数据
    private Object data;

    public static ResponseResult build(Integer code, String msg) {
        return new ResponseResult(code, msg);
    }

    public static ResponseResult build(Integer code, String msg, Object data) {
        return new ResponseResult(code, msg, data);
    }

    public static ResponseResult build(ResponseResultCode responseResultCode) {
        return new ResponseResult(responseResultCode.getCode(), responseResultCode.getMsg());
    }

    public static ResponseResult ok() {
        return new ResponseResult(ResponseResultCode.SUCCESS.getCode(), ResponseResultCode.SUCCESS.getMsg());
    }

    public static ResponseResult ok(Object data) {
        return new ResponseResult(ResponseResultCode.SUCCESS.getCode(), ResponseResultCode.SUCCESS.getMsg(), data);
    }

    public ResponseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}