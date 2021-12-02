package com.melon.message.service.impl;

import com.melon.common.pojo.FetchParams;
import com.melon.common.pojo.Response;
import com.melon.message.mapper.QueryMessageMapper;
import com.melon.message.pojo.Message;
import com.melon.message.service.FetchMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FetchMessageServiceImpl implements FetchMessageService {

    @Autowired
    private QueryMessageMapper messageMapper;

    @Override
    public Response fetchMessageUnread(String userId) {
        Message[] messages = messageMapper.fetchMessageUnread(userId);
        log.info("fetch unread messages of user {} successfully", userId);
        return Response.success("拉取未读消息成功", messages);
    }

    @Override
    public Response fetchMessageAll(FetchParams fetchParams) {
        if (fetchParams.getTimestamp() == null) {
            if (fetchParams.getPage() > 1) {
                return Response.error("参数错误：timestamp 不能为空");
            }
            fetchParams.setTimestamp(messageMapper.getCurrentTimestamp());
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("timestamp", fetchParams.getTimestamp());

        Message[] messages = messageMapper.fetchMessageAll(fetchParams);
        log.info("fetch messages successfully, params: {}", fetchParams.toString());

        resp.put("messages", messages);
        return Response.success("拉取成功", resp);
    }
}
