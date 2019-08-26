package com.rose.advice;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rose.common.data.response.ResponseResult;
import com.rose.common.data.response.ResponseResultCode;
import com.rose.common.exception.BusinessException;
import com.rose.common.exception.SentinelCaputeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 功能：controller 加强类
 * @author sunpeng
 * @date 2017
 */
@Slf4j
@ControllerAdvice(basePackages = {"com.rose.controler"})
public class ExceptionControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return ResponseResult.ok(body);
    }

    @ResponseBody
    @ExceptionHandler()
    public Object handler(BusinessException e) {
        log.error("【BusinessException】返回错误信息：{}", e.msg);
        return ResponseResult.build(e.code, e.msg);
    }

    @ResponseBody
    @ExceptionHandler()
    public Object handler(BlockException e) {
        log.error("【BlockException】返回错误信息：{}", e);
        return ResponseResult.build(ResponseResultCode.SERVER_BUSY_ERROE);
    }

    @ResponseBody
    @ExceptionHandler()
    public Object handler(SentinelCaputeException e) {
        log.error("【SentinelCaputeException】返回错误信息：{}", e);
        return ResponseResult.build(e.code, e.msg);
    }

    @ResponseBody
    @ExceptionHandler()
    public Object handler(Exception e) {
    	log.error("【Exception】返回错误信息：{}", e);
        return ResponseResult.build(ResponseResultCode.SERVER_ERROR);
    }
}