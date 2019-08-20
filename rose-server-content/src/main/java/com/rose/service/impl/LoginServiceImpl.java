package com.rose.service.impl;

import com.rose.common.data.response.ResponseResult;
import com.rose.common.data.response.ResponseResultCode;
import com.rose.common.exception.BusinessException;
import com.rose.common.repository.RedisRepositoryCustom;
import com.rose.common.util.HttpRequestUtil;
import com.rose.common.util.RedisKeyUtil;
import com.rose.common.util.StringUtil;
import com.rose.common.util.ValueHolder;
import com.rose.data.constant.SystemConstant;
import com.rose.data.entity.TbSysUser;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.dbopt.mapper.TbSysUserMapper;
import com.rose.service.LoginService;
import com.rose.service.feign.FeignLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Inject
    private FeignLoginService feignLoginService;
    @Inject
    private TbSysUserMapper tbSysUserMapper;
    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;
    @Inject
    private ValueHolder valueHolder;

    @Override
    public Object verify(UserLoginDto param) throws Exception {
        ResponseResult res = feignLoginService.verify(param);
        if (res == null) {
            throw new BusinessException(ResponseResultCode.OPERT_ERROR);
        }
        if (!ResponseResultCode.SUCCESS.getCode().equals(res.getCode())) {
            throw new BusinessException(res);
        }
        return res.getData();
    }

    @Override
    public String toSuccess(HttpServletRequest request) {
        String userId = HttpRequestUtil.getValueByHeaderOrParam(request, SystemConstant.SYSTEM_USER_ID);
        String token = HttpRequestUtil.getValueByHeaderOrParam(request, SystemConstant.SYSTEM_TOKEN_NAME);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(token)) {
            throw new BusinessException(ResponseResultCode.PARAM_ERROR);
        }
        ResponseResult res = feignLoginService.tokenValidate(Long.valueOf(userId), token);
        if (res == null) {
            throw new BusinessException(ResponseResultCode.OPERT_ERROR);
        }
        if (!ResponseResultCode.SUCCESS.getCode().equals(res.getCode())) {
            return "login";
        }
        TbSysUser sysUser = tbSysUserMapper.selectByPrimaryKey(valueHolder.getUserIdHolder());
        if (sysUser == null) {
            return "login";
        }
        request.setAttribute("uname", sysUser.getUname());
        return "home";
    }

    @Override
    public String out(Long userId, String token) {
        ResponseResult res = feignLoginService.tokenValidate(userId, token);
        if (res == null) {
            throw new BusinessException(ResponseResultCode.OPERT_ERROR);
        }
        if (ResponseResultCode.SUCCESS.getCode().equals(res.getCode())) {
            redisRepositoryCustom.delete(RedisKeyUtil.getRedisUserInfoKey(userId));
        }
        return "login";
    }
}