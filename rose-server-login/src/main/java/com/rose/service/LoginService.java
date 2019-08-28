package com.rose.service;

import com.rose.data.to.dto.UserLoginDto;

import java.util.Map;

/**
 * 功能：登录 service
 * @author sunpeng
 * @date 2019
 */
public interface LoginService {

    /**
     * 功能：登录
     * @param param
     * @return
     * @throws Exception
     */
    Map<String, Object> verify(UserLoginDto param) throws Exception;

    /**
     * 功能：退出
     * @param userId
     * @param token
     */
    void out(Long userId, String token);

    /**
     * 功能：token校验，若通过校验，并更新token有效时长
     * @param userId
     * @param token
     * @return true：通过校验，false：未通过校验
     */
    boolean tokenValidate(Long userId, String token);
}