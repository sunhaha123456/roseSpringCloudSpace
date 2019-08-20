package com.rose.service;

import com.rose.data.to.dto.UserLoginDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 功能：登录 service
 * @author sunpeng
 * @date 2019
 */
public interface LoginService {

    String toSuccess(HttpServletRequest request);

    /**
     * 功能：登出
     * @param userId
     * @param token
     * @return
     * @throws Exception
     */
    String out(Long userId, String token);

    /**
     * 功能：登录
     * @param param
     * @return
     * @throws Exception
     */
    Object verify(UserLoginDto param) throws Exception;
}