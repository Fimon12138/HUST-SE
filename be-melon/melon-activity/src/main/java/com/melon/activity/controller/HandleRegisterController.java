package com.melon.activity.controller;

import com.melon.activity.pojo.params.FetchRegisterParams;
import com.melon.activity.pojo.params.UpdateRegisterParams;
import com.melon.activity.service.HandleRegisterService;
import com.melon.common.pojo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@Api(tags = "处理报名")
@RequestMapping("/register/handle")
public class HandleRegisterController {

    @Autowired
    private HandleRegisterService handleRegisterService;

    @ApiOperation(value = "拉取报名信息")
    @PostMapping("/fetch")
    public Response fetchRegister(
        @RequestBody @Valid FetchRegisterParams params,
        HttpServletRequest request
    ) {
        if (params.getTimestamp() == null && params.getPage() > 1) {
            return Response.error("参数错误：timestamp不能为空");
        }

        String userId = request.getHeader("userId");
        log.info("user {} tries to fetch register info of activity {}",
                userId, params.getActivityId());
        return handleRegisterService.fetchRegister(userId, params);
    }

    @ApiOperation(value = "获取单个报名详情")
    @GetMapping("/details/{registerId}")
    public Response getRegisterDetails(@PathVariable("registerId") String registerId) {
        log.info("get details of register {}", registerId);
        return handleRegisterService.getRegisterDetails(registerId);
    }

    @ApiOperation(value = "处理报名请求")
    @PostMapping("")
    public Response updateRegisterStatus(
            @RequestBody UpdateRegisterParams params
            ) {
        log.info("try to update status of register {} to {}",
                params.getRegisterId(), params.getStatus());
        return handleRegisterService.updateRegisterStatus(params.getRegisterId(),
                params.getStatus(), params.getMessage());
    }
}
