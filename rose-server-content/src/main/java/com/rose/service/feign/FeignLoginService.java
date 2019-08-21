package com.rose.service.feign;

import com.rose.common.data.response.ResponseResult;
import com.rose.data.to.dto.UserLoginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "rose-gateway-server")
public interface FeignLoginService {

    /**
     * 功能：登录
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/rose-gateway-server/login/verify")
    ResponseResult verify(@RequestBody UserLoginDto param);

    /**
     * 功能：退出
     * @param userId
     * @param token
     */
    @GetMapping(value = "/rose-gateway-server/login/out")
    ResponseResult out(@RequestParam(value = "userId") Long userId, @RequestParam(value = "token") String token);

    /**
     * 功能：token 校验
     * @param userId
     * @param token
     */
    @GetMapping(value = "/rose-gateway-server/login/tokenValidate")
    ResponseResult tokenValidate(@RequestParam(value = "userId") Long userId, @RequestParam(value = "token") String token);
}