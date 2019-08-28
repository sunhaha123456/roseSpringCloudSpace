package com.rose.controler;

import com.rose.common.data.response.ResponseResultCode;
import com.rose.common.exception.BusinessException;
import com.rose.data.to.dto.UserLoginDto;
import com.rose.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * 功能：登录 controller
 * @author sunpeng
 * @date 2019
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginControler {

    @Inject
    private LoginService loginService;

    /**
     * 功能：登录
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/verify")
    public Map verify(@RequestBody @Validated(UserLoginDto.BaseInfo.class) UserLoginDto param) throws Exception {
        return loginService.verify(param);
    }

    /**
     * 功能：退出
     * @param userId
     * @param token
     */
    @GetMapping(value = "/out")
    public void out(@RequestParam Long userId, @RequestParam String token) {
        loginService.out(userId, token);
    }

    /**
     * 功能：token 校验
     * @param userId
     * @param token
     */
    @GetMapping(value = "/tokenValidate")
    public void tokenValidate(@RequestParam Long userId, @RequestParam String token) {
        boolean flag = loginService.tokenValidate(userId, token);
        if (!flag) {
            throw new BusinessException(ResponseResultCode.VALIDATE_ERROR);
        }
    }
}