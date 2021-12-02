package com.melon.user.controller;

import com.melon.common.pojo.Response;
import com.melon.user.pojo.UpdateParams;
import com.melon.user.service.UpdateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(tags = "更新用户信息")
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    private UpdateService updateService;


    @ApiOperation(value = "更新用户头像")
    @PostMapping("/avatar")
    public Response updateAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        String userId = request.getHeader("userId");
        log.info("update avatar, user: {}", userId);
        return updateService.updateAvatar(userId, file);
    }


    @ApiOperation(value = "更新用户信息")
    @PostMapping("/details")
    public Response updateUserInfo(@RequestBody UpdateParams updateParams, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        log.info("update user details, user: {}, details: {}", userId, updateParams.toString());
        return updateService.updateUserDetails(userId, updateParams);
    }


}
