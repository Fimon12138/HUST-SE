package com.melon.user.controller;

import com.melon.common.pojo.Response;
import com.melon.user.pojo.LoginParams;
import com.melon.user.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Api(tags = "用户注册")
@Slf4j
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @ApiOperation(value = "用户名是否可用")
    @GetMapping("/check/{username}")
    public Response isUsernameRegistered(
            @PathVariable(value = "username") @NotBlank String username
    ) {
        return registerService.isUsernameRegistered(username);
    }


    @ApiOperation(value = "注册")
    @PostMapping("")
    public Response register(@RequestBody @Valid LoginParams loginParams) {
        log.info("user register, username: {}", loginParams.getUsername());
        return registerService.register(loginParams);
    }

}
