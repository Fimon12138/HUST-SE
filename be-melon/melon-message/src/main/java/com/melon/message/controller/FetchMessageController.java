package com.melon.message.controller;

import com.melon.common.pojo.FetchParams;
import com.melon.common.pojo.Response;
import com.melon.message.service.FetchMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(tags = "拉取消息")
@RequestMapping("/fetch")
public class FetchMessageController {

    @Autowired
    private FetchMessageService fetchMessageService;

    @ApiOperation("拉取未读消息")
    @GetMapping("")
    public Response fetchMessageUnread(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        log.info("fetch unread message of user {}", userId);
        return fetchMessageService.fetchMessageUnread(userId);
    }

    @ApiOperation("拉取全部消息")
    @PostMapping("/all")
    public Response fetchMessageAll(
            @RequestBody FetchParams fetchParams,
            HttpServletRequest request
            ) {
        String userId = request.getHeader("userId");
        fetchParams.setUserId(userId);
        log.info("fetch messages: params: {}", fetchParams.toString());
        return fetchMessageService.fetchMessageAll(fetchParams);
    }

}
