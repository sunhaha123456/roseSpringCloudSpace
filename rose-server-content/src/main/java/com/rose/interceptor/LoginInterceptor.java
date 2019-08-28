package com.rose.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.rose.common.data.response.ResponseResult;
import com.rose.common.data.response.ResponseResultCode;
import com.rose.common.util.HttpRequestUtil;
import com.rose.common.util.ValueHolder;
import com.rose.data.constant.SystemConstant;
import com.rose.service.feign.FeignLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 功能：登录拦截器
 * @author sunpeng
 * @date 2019
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Inject
    private FeignLoginService feignLoginService;

    @Inject
    private ValueHolder valueHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String url = request.getRequestURI();
        String userId = HttpRequestUtil.getValueByHeaderOrParam(request, SystemConstant.SYSTEM_USER_ID);
        String token = HttpRequestUtil.getValueByHeaderOrParam(request, SystemConstant.SYSTEM_TOKEN_NAME);
        /*
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(token)) {
            log.error("参数错误！url：{}，userId：{}，token：{}", url, userId, token);
            getFail(response);
            return false;
        }
        ResponseResult res = feignLoginService.tokenValidate(Long.valueOf(userId), token);
        if (res == null) {
            log.error("交互失败！url：{}，userId：{}，token：{}", url, userId, token);
            getFail(response);
            return false;
        }
        if (!ResponseResultCode.SUCCESS.getCode().equals(res.getCode())) {
            log.error("验证失败！url：{}，userId：{}，token：{}", url, userId, token);
            getFail(response);
            return false;
        }
        */
        request.setAttribute(SystemConstant.SYSTEM_TOKEN_NAME, token);
        request.setAttribute(SystemConstant.SYSTEM_USER_ID, userId);
        valueHolder.setTokenHolder(token);
        valueHolder.setUserIdHolder(Long.valueOf(userId));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    // 设置返回的失败信息
    private void getFail(HttpServletResponse response) {
        //将实体对象转换为JSON Object转换
        String json = JSONObject.toJSONString(ResponseResult.build(ResponseResultCode.LOGIN_FIRST));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}