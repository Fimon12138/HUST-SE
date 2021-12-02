package com.melon.user.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.melon.common.pojo.Response;
import com.melon.common.pojo.UserDetails;
import com.melon.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(tags = "用户信息操作")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户信息")
    @GetMapping("/details")
    public Response getUserDetails(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        log.info("get user details of user {}", userId);
        return userService.getUserDetails(userId);
    }


}
