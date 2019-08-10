package com.rose.data.to.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能：用户信息主要指：token、用户状态
 * @author sunpeng
 * @date 2019
 */
@Data
public class UserRedisVo implements Serializable {
    // 用户token
    private String token;

    public UserRedisVo() {
    }

    public UserRedisVo(String token) {
        this.token = token;
    }
}