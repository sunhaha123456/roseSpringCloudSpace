package com.rose.controler;

import com.rose.common.repository.RedisRepositoryCustom;
import com.rose.common.util.ValueHolder;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.dbopt.mapper.TbSysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 功能：登录 controller
 * @author sunpeng
 * @date 2019
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginControler {

    @Inject
    private TbSysUserMapper tbSysUserMapper;
    @Inject
    private RedisRepositoryCustom redisRepositoryCustom;
    @Inject
    private ValueHolder valueHolder;

    @GetMapping(value = "/toLogin")
    public String toLogin() {
        return "login";
    }

    @GetMapping(value = "/toSuccess")
    public String toSuccess(HttpServletRequest request) throws Exception {
//        if (loginService.tokenValidate(request)) {
//            TbSysUser sysUser = tbSysUserMapper.selectByPrimaryKey(valueHolder.getUserIdHolder());
//            if (sysUser != null) {
//                request.setAttribute("uname", sysUser.getUname());
//                return "home";
//            }
//        }
        return "login";
    }

    @GetMapping(value = "/out")
    public String out(HttpServletRequest request) throws Exception {
//        if (loginService.tokenValidate(request)) {
//            redisRepositoryCustom.delete(RedisKeyUtil.getRedisUserInfoKey(valueHolder.getUserIdHolder()));
//        }
        return "login";
    }

    @ResponseBody
    @PostMapping(value = "/verify")
    public Map verify(@RequestBody @Validated(UserLoginDto.BaseInfo.class) UserLoginDto param) throws Exception {
//        return loginService.verify(param);
        return null;
    }
}