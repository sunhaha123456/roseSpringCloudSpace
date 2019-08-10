package com.rose.service;

import com.rose.data.to.vo.UserRedisVo;

/**
 * 功能：user service
 * @author sunpeng
 * @date 2019
 */
public interface UserService {
    void userRedisInfoSave(String redisKey, UserRedisVo userRedis);
}