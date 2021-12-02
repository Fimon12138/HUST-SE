package com.melon.message.controller;

import com.melon.common.pojo.Response;
import com.melon.message.service.HandleMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(tags = "处理消息")
@RequestMapping("/read")
public class ReadMessageController {

    @Autowired
    private HandleMessageService handleMessageService;

    @ApiOperation("已读消息")
    @GetMapping("/{messageId}")
    public Response readMessage(
            @PathVariable("messageId") String messageId,
            HttpServletRequest request
    ) {
        String userId = request.getHeader("userId");
        log.info("user {} read message {}", userId, messageId);
        return handleMessageService.readMessage(userId, messageId);
    }

}
