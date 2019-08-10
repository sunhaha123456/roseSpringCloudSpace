package com.rose.service.impl;

import com.rose.common.data.response.ResponseResultCode;
import com.rose.common.exception.BusinessException;
import com.rose.common.repository.RedisRepositoryCustom;
import com.rose.common.util.IdUtil;
import com.rose.common.util.Md5Util;
import com.rose.common.util.StringUtil;
import com.rose.common.util.ValueHolder;
import com.rose.data.constant.SystemConstant;
import com.rose.data.entity.TbSysUser;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.data.to.vo.UserRedisVo;
import com.rose.dbopt.mapper.TbSysUserMapper;
import com.rose.service.LoginService;
import com.rose.service.UserService;
import com.rose.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
    @Inject
    private UserService userService;
    @Inject
    private ValueHolder valueHolder;

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
        userService.userRedisInfoSave(RedisKeyUtil.getRedisUserInfoKey(user.getId()), userRedis);
        // 4、删除redis验证码
        redisRepositoryCustom.delete(SystemConstant.LOGIN_CODE_PREFIX + param.getKey());
        Map<String, Object> res = new HashMap();
        res.put("token", userRedis.getToken());
        res.put("userId", user.getId());
        return res;
    }

    @Override
    public void out() throws Exception {
        redisRepositoryCustom.delete(valueHolder.getTokenHolder());
    }

    @Override
    public boolean tokenValidate(HttpServletRequest request) {
        String method = request.getMethod();
        if ("OPTIONS".equals(method.toUpperCase())) {
            return true;
        }
        valueHolder.removeAll();
        String token = request.getHeader(SystemConstant.SYSTEM_TOKEN_NAME);
        String userId = request.getHeader(SystemConstant.SYSTEM_USER_ID);
        if (StringUtil.isEmpty(token)) {
            token = request.getParameter(SystemConstant.SYSTEM_TOKEN_NAME);
        }
        if (StringUtil.isEmpty(userId)) {
            userId = request.getParameter(SystemConstant.SYSTEM_USER_ID);
        }
        String url = request.getRequestURI();
        if (StringUtil.isEmpty(token) || token.length() != 64 || StringUtil.isEmpty(userId)) {
            log.error("Request url：{}，method：{}，userId：{}，token：{}，拦截此请求：001-请求不合法！", url, method, userId, token);
            return false;
        }
        UserRedisVo userRedis = redisRepositoryCustom.getClassObj(RedisKeyUtil.getRedisUserInfoKey(userId), UserRedisVo.class);
        if (userRedis == null) {
            log.error("Request url：{}，method：{}，userId：{}，token：{}，拦截此请求：002-redis中userId对应键值已超时！", url, method, userId, token);
            return false;
        }
        if (!token.equals(userRedis.getToken())) {
            log.error("Request url：{}，method：{}，userId：{}，token：{}，拦截此请求：003-redis中userId对应redis中用户信息的token，与前端传入token，不一致！", url, method, userId, token);
            return false;
        }
        userService.userRedisInfoSave(RedisKeyUtil.getRedisUserInfoKey(userId), userRedis);
        request.setAttribute(SystemConstant.SYSTEM_TOKEN_NAME, token);
        request.setAttribute(SystemConstant.SYSTEM_USER_ID, userId);
        valueHolder.setTokenHolder(token);
        valueHolder.setUserIdHolder(Long.valueOf(userId));
        return true;
    }
}