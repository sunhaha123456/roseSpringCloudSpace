package com.rose.service.impl;

import com.rose.common.repository.RedisRepositoryCustom;
import com.rose.common.util.JsonUtil;
import com.rose.data.constant.SystemConstant;
import com.rose.data.to.vo.UserRedisVo;
import com.rose.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;

    @Override
    public void userRedisInfoSave(String redisKey, UserRedisVo userRedis) {
        redisRepositoryCustom.saveMinutes(redisKey, JsonUtil.objectToJson(userRedis), SystemConstant.LOGIN_USER_INFO__SAVE_TIME);
    }
}