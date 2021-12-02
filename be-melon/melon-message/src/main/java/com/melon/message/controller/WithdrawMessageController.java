package com.melon.message.controller;

import com.melon.common.pojo.Response;
import com.melon.message.service.HandleMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(tags = "撤回消息")
@RequestMapping("/withdraw")
public class WithdrawMessageController {

    @Autowired
    private HandleMessageService handleMessageService;

    // 用户撤回私发消息
    @ApiOperation("撤回消息")
    @GetMapping("/{messageId}")
    public Response withdrawMessage(
            @PathVariable("messageId") String messageId,
            HttpServletRequest request
    ) {
        String userId = request.getHeader("userId");
        log.info("user {} tries to withdraw message {}", userId, messageId);
        return handleMessageService.withdrawMessage(userId, messageId);
    }


    // 远程调用，撤回站内信
    @GetMapping("/feign/{messageId}")
    public boolean withdrawMessageFeign(
            @PathVariable("messageId") String messageId
    ) {
        log.info("feign: withdraw message {}", messageId);
        return handleMessageService.withdrawMessageFeign(messageId);
    }

}
