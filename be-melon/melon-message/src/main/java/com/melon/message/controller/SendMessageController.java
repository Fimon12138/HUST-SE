package com.melon.message.controller;

import com.melon.common.pojo.Response;
import com.melon.common.pojo.message.TransferMessage;
import com.melon.message.pojo.SendMessageParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/send")
public class SendMessageController {

    @ApiOperation("发送私信")
    @PostMapping("")
    public Response sendMessage(
            @RequestBody @Valid SendMessageParams params,
            HttpServletRequest request
    ) {
        String userId = request.getHeader("userId");

    }

}
