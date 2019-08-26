package com.rose.common.exception;

import com.rose.common.data.response.ResponseResult;
import com.rose.common.data.response.ResponseResultCode;

/**
 * 功能：
 *      专用于sentinel 捕获的异常类
 * @author sunpeng
 * @date 2017
 */
public class SentinelCaputeException extends RuntimeException {
    public int code;

    public String msg;

    public SentinelCaputeException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SentinelCaputeException(String msg) {
        this.code = 500;
        this.msg = msg;
    }

    public SentinelCaputeException(ResponseResultCode res) {
        this.code = res.getCode();
        this.msg = res.getMsg();
    }

    public SentinelCaputeException(ResponseResult res) {
        this.code = res.getCode();
        this.msg = res.getMsg();
    }
}