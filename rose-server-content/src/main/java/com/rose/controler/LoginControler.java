package com.rose.controler;

import com.rose.common.util.HttpRequestUtil;
import com.rose.common.util.StringUtil;
import com.rose.data.constant.SystemConstant;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
    private LoginService loginService;

    @GetMapping(value = "/toLogin")
    public String toLogin() throws InterruptedException {
        Thread.sleep(10000000000L);
        return "login";
    }

    @ResponseBody
    @PostMapping(value = "/verify")
    public Object verify(@RequestBody @Validated(UserLoginDto.BaseInfo.class) UserLoginDto dto) throws Exception {
        return loginService.verify(dto);
    }

    @GetMapping(value = "/toSuccess")
    public String toSuccess(HttpServletRequest request) {
        return loginService.toSuccess(request);
    }

    @GetMapping(value = "/out")
    public String out(HttpServletRequest request) {
        String userId = HttpRequestUtil.getValueByHeaderOrParam(request, SystemConstant.SYSTEM_USER_ID);
        String token = HttpRequestUtil.getValueByHeaderOrParam(request, SystemConstant.SYSTEM_TOKEN_NAME);
        if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(token)) {
            return "login";
        }
        return loginService.out(Long.valueOf(userId), token);
    }
}