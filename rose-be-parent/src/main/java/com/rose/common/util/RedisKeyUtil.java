package com.rose.common.util;

import com.rose.data.constant.SystemConstant;

/**
 * 功能：生产redis key工具类
 * @author sunpeng
 * @date 2019
 */
public class RedisKeyUtil {
    // 获取userId 对应redis中用户信息key
    public static String getRedisUserInfoKey(Object userId) {
        return SystemConstant.LOGIN_USER_INFO_PREFIX + userId;
    }
}