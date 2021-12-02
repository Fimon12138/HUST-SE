package com.melon.message.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.melon.common.pojo.message.MessageType;
import com.melon.common.pojo.message.TransferMessage;
import com.melon.message.feign.ActivityFeignService;
import com.melon.message.feign.UserFeignService;
import com.melon.message.mapper.HandleMessageMapper;
import com.melon.message.mapper.QueryMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegisterMessageConsumer {

    @Autowired
    private HandleMessageMapper messageMapper;

    @Autowired
    private ActivityFeignService activityFeignService;

    @Autowired
    private UserFeignService userFeignService;

    @KafkaListener(topics = "send_register", groupId = "group0")
    public void handleSendRegisterMessage(ConsumerRecord<String, String> record) {
        String value = record.value();
        log.info("handle send register message invoked: {}", value);

        TransferMessage transferMessage = JSON.parseObject(value, new TypeReference<TransferMessage>(){});
        JSONObject reference = JSONObject.parseObject(transferMessage.getReference());
        String messageId = transferMessage.getId();

        // 需要添加 toUserId 和 hint
        // reference 中有 activityId 和 registerId
        JSONObject activity = null;
        String activityId = reference.getString("activityId");
        try {
            String activityDetails = activityFeignService.queryActivityDetails(activityId);
            activity = JSONObject.parseObject(activityDetails);
        } catch (Exception e) {
            log.error("openfeign error: melon-message -> melon-activity: /feign/details");
            log.error("handle message error, id: {}", messageId);
            return;
        }
        if (activity == null) {
            log.error("handle message {} error, can't find related activity {}",
                    messageId, activityId);
            return;
        }

        JSONObject user = null;
        String userId = transferMessage.getFromUserId();
        try {
            String userDetails = userFeignService.queryUserDetails(userId);
            user = JSONObject.parseObject(userDetails);
        } catch (Exception e) {
            log.error("openfeign error: melon-message -> melon-user: /feign/details");
            log.error("handle message error, id: {}", messageId);
            return;
        }
        if (user == null) {
            log.error("handle message {} error, can't find related user {}",
                    messageId, userId);
            return;
        }

        // 添加 toUserId 信息
        transferMessage.setToUserId(activity.getString("userId"));
        // 添加 hint 信息
        String hint = String.format("%s %s %s，请及时处理！",
                user.getString("username"),
                transferMessage.getType() == MessageType.INITREGISTER.val ? "报名了活动" : "取消报名活动",
                activity.getString("title"));
        transferMessage.setHint(hint);

        messageMapper.insertMessage(transferMessage);
        log.info("send message successfully, id: {}", messageId);
        return;
    }


    @KafkaListener(topics = "reply_register", groupId = "group0")
    public void handleReplyRegisterMessage(ConsumerRecord<String, String> record) {
        String value = record.value();
        log.info("handle reply register message invoked: {}", value);

        TransferMessage transferMessage = JSON.parseObject(value, new TypeReference<TransferMessage>(){});
        JSONObject reference = JSONObject.parseObject(transferMessage.getReference());
        String messageId = transferMessage.getId();

        // 需要添加 fromUserId 和 toUserId 和 hint
        // reference 中有：registerId
        JSONObject register = null;
        String registerId = reference.getString("registerId");
        try {
            String registerDetails = activityFeignService.queryRegisterDetails(registerId);
            register = JSONObject.parseObject(registerDetails);
        } catch (Exception e) {
            log.error("openfeign error: melon-message -> melon-activity: /feign/register/details");
            log.error("handle message error, id: {}", messageId);
            return;
        }
        if (register == null) {
            log.error("handle message {} error: can't find related register {}",
                    messageId, registerId);
            return;
        }

        JSONObject activity = null;
        String activityId = register.getString("activityId");
        try {
            String activityDetails = activityFeignService.queryActivityDetails(activityId);
            activity = JSONObject.parseObject(activityDetails);
        } catch (Exception e) {
            log.error("openfeign error: melon-message -> melon-activity: /feign/details");
            log.error("handle message error, id: {}", transferMessage.getId());
            return;
        }
        if (activity == null) {
            log.error("handle message {} error, can't find related activity {}",
                    messageId, activityId);
            return;
        }

        // fromUserId
        transferMessage.setFromUserId(register.getString("userId"));
        // toUserId
        transferMessage.setToUserId(activity.getString("userId"));
        // hint
        String hint = null;
        String activityName = activity.getString("title");
        int messageType = transferMessage.getType();
        switch (messageType) {
            case 1:
                hint = String.format("活动 %s 的发起人已收到你的报名申请！", activityName);
                break;
            case 2:
                hint = String.format("活动 %s 的发起人已通过了你的报名申请，联系他获取更多信息！", activityName);
                break;
            case 3:
                hint = String.format("活动 %s 的发起人拒绝了你的报名申请！", activityName);
                break;
            case 5:
                hint = String.format("活动 %s 的发起人取消了活动！", activityName);
                break;
            default:
                log.error("unexpected message type: {}", messageType);
                log.error("handle message error, id: {}", messageId);
                return;
        }
        transferMessage.setHint(hint);

        messageMapper.insertMessage(transferMessage);
        log.info("send message successfully, id: {}", messageId);
        return;
    }

}
