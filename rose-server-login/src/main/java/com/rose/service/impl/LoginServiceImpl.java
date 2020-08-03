package com.rose.service.impl;

import com.rose.common.data.response.ResponseResultCode;
import com.rose.common.exception.BusinessException;
import com.rose.common.repository.RedisRepositoryCustom;
import com.rose.common.util.*;
import com.rose.data.constant.SystemConstant;
import com.rose.data.entity.TbSysUser;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.data.to.vo.UserRedisVo;
import com.rose.dbopt.mapper.TbSysUserMapper;
import com.rose.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Inject
    private TbSysUserMapper tbUserRepository;
    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;

    @Override
    public Map<String, Object> verify(UserLoginDto param) throws Exception {
        // 1、校验验证码
        String codeRedis = redisRepositoryCustom.getString(SystemConstant.LOGIN_CODE_PREFIX + param.getKey());
        String codeFront = Md5Util.MD5Encode(param.getCode());
        if (StringUtil.isEmpty(codeRedis) || StringUtil.isEmpty(codeFront) || !codeRedis.equals(codeFront)) {
            throw new BusinessException(ResponseResultCode.CODE_ERROR);
        }
        // 2、校验用户名和密码，并且用户状态正常
        List<TbSysUser> userList = tbUserRepository.listByUnameAndPwd(param.getUname(), Md5Util.MD5Encode(param.getUpwd()));
        if (userList == null || userList.size() == 0) {
            throw new BusinessException(ResponseResultCode.LOGIN_ERROR);
        }
        TbSysUser user = userList.get(0);
        // 3、更新redis用户信息，更新用户token、用户状态
        UserRedisVo userRedis = new UserRedisVo(IdUtil.getID() + IdUtil.getID());
        userRedisInfoSave(RedisKeyUtil.getRedisUserInfoKey(user.getId()), userRedis);
        // 4、删除redis验证码
        redisRepositoryCustom.delete(SystemConstant.LOGIN_CODE_PREFIX + param.getKey());
        Map<String, Object> res = new HashMap();
        res.put("token", userRedis.getToken());
        res.put("userId", user.getId());
        return res;
    }

    @Override
    public void out(Long userId, String token) {
        if (tokenValidate(userId, token)) {
            redisRepositoryCustom.delete(RedisKeyUtil.getRedisUserInfoKey(userId));
        } else {
            throw new BusinessException(ResponseResultCode.LOGIN_FIRST);
        }
    }

    @Override
    public boolean tokenValidate(Long userId, String token) {
        if (userId == null || StringUtil.isEmpty(token) || token.length() != 64) {
            log.error("userId：{}，token：{}，拦截，参数错误", userId, token);
            return false;
        }
        UserRedisVo userRedis = redisRepositoryCustom.getClassObj(RedisKeyUtil.getRedisUserInfoKey(userId), UserRedisVo.class);
        if (userRedis == null) {
            log.error("userId：{}，token：{}，拦截，redis中userId对应键值已超时", userId, token);
            return false;
        }
        if (!token.equals(userRedis.getToken())) {
            log.error("userId：{}，token：{}，拦截，redis中userId对应redis中用户信息的token，与前端传入token，不一致", userId, token);
            return false;
        }
        userRedisInfoSave(RedisKeyUtil.getRedisUserInfoKey(userId), userRedis);
        log.info("userId：{}，token：{}，放行", userId, token);
        return true;
    }

    private void userRedisInfoSave(String redisKey, UserRedisVo userRedis) {
        redisRepositoryCustom.saveMinutes(redisKey, JsonUtil.objectToJson(userRedis), SystemConstant.LOGIN_USER_INFO__SAVE_TIME);
    }
}