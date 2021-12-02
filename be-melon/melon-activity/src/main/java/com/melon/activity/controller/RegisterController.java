package com.melon.activity.controller;

import com.melon.activity.pojo.params.RegisterParams;
import com.melon.common.pojo.Response;
import com.melon.activity.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;


@Slf4j
@RestController
@Api(tags = "活动报名")
@RequestMapping("/register")
public class RegisterController {

  @Autowired
  private RegisterService registerService;

  @ApiOperation(value = "报名活动")
  @PostMapping("")
  public Response registerActivity(
    @RequestBody @Valid RegisterParams params,
    HttpServletRequest request
  ) {
    String userId = request.getHeader("userId");
    log.info("user {} tries to register to activity {}", userId, params.getActivityId());
    return registerService.initRegister(userId, params);
  }


  @ApiOperation(value = "取消活动报名")
  @PostMapping("/cancel")
  public Response cancelRegisterActivity(
    @RequestBody @Valid RegisterParams params,
    HttpServletRequest request
  ) {
    String userId = request.getHeader("userId");
    log.info("user {} tries to cancel registration to activity {}",
      userId, params.getActivityId());
    return registerService.cancelRegister(userId, params);
  }



}
