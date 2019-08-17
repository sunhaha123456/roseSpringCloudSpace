package com.rose.data.to.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能：用户信息主要指：token、用户状态
 * @author sunpeng
 * @date 2019
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRedisVo implements Serializable {
    // 用户token
    private String token;
}