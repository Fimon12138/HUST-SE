package com.melon.message.service.impl;

import com.melon.common.pojo.Response;
import com.melon.common.pojo.message.MessageType;
import com.melon.common.pojo.message.TransferMessage;
import com.melon.common.tools.IdGenerator;
import com.melon.message.mapper.HandleMessageMapper;
import com.melon.message.mapper.QueryMessageMapper;
import com.melon.message.pojo.MessageStatus;
import com.melon.message.pojo.SendMessageParams;
import com.melon.message.service.HandleMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class HandleMessageServiceImpl implements HandleMessageService {

    @Autowired
    private QueryMessageMapper queryMessageMapper;

    @Autowired
    private HandleMessageMapper handleMessageMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public Response sendMessage(String userId, SendMessageParams params) {
        TransferMessage message = new TransferMessage(idGenerator.nextId(),
                MessageType.USERMESSAGE.val, userId, params.getToUserId(),
                "用户 " + ,
                params.getMessage(), null);
        return null;
    }

    private boolean validateAccess(String userId, String messageId) {
        if (userId == null) {
            return false;
        }
        String toUserId = queryMessageMapper.getToUserIdByMessageId(messageId);
        return userId.equals(toUserId);
    }

    @Override
    public Response readMessage(String userId, String messageId) {
        if (validateAccess(userId, messageId)) {
            handleMessageMapper.updateMessageStatus(messageId, MessageStatus.READ.val);
            log.info("user {} read message {} successfully", userId, messageId);
            return Response.success("已读");
        }
        log.error("user {} read message {} fail: doesn't belong to him", userId, messageId);
        return Response.error("该消息不属于此用户");
    }

    @Override
    public boolean withdrawMessageFeign(String messageId) {
        int num = queryMessageMapper.getMessageNum(messageId);
        if (1 == num) {
            handleMessageMapper.updateMessageStatus(messageId, MessageStatus.WITHDRAW.val);
            log.info("withdraw message {} successfully", messageId);
            return true;
        } else {
            // 该消息可能在队列中待处理
            redisUtils.withdrawMessage(messageId);
            log.info("message {} may still in the queue, add withdraw command to redis", messageId);
            return true;
        }
    }

    @Override
    public Response withdrawMessage(String userId, String messageId) {
        if (validateAccess(userId, messageId)) {
            handleMessageMapper.updateMessageStatus(messageId, MessageStatus.WITHDRAW.val);
            log.info("user {} withdraw message {} successfully", userId, messageId);
            return Response.success("撤回成功");
        }
        log.error("user {} have no access to message {}, withdraw fail", userId, messageId);
        return Response.error("用户无权操作该消息");
    }

    @Override
    public Response deleteMessage(String userId, String messageId) {
        if (validateAccess(userId, messageId)) {
            handleMessageMapper.updateMessageStatus(messageId, MessageStatus.DELETE.val);
            log.info("user {} delete message {} successfully", userId, messageId);
            return Response.success("删除成功");
        }
        log.error("user {} have no access to message {}, delete fail", userId, messageId);
        return Response.error("用户无权操作该消息");
    }
}
