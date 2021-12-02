package com.melon.user.controller;

import com.melon.common.pojo.Response;
import com.melon.user.pojo.LoginParams;
import com.melon.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags = "登录控制")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Response login(@RequestBody @Valid LoginParams loginParams) {
        log.info("user login request, username: {}", loginParams.getUsername());
        return loginService.login(loginParams);
    }


    // 退出时直接返回，前端删除 token
    @ApiOperation(value = "注销")
    @GetMapping("/logout")
    public Response logout() {
        return Response.success("注销成功");
    }

}
