package com.melon.activity.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.melon.common.pojo.message.MessageType;
import com.melon.common.pojo.message.TransferMessage;
import com.melon.common.tools.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private IdGenerator idGenerator;

    // 报名人 -> 活动所有者
    public void sendRegisterMessage(Integer type, String userId, String message, String activityId,
                                     String registerId) {
        JSONObject obj = new JSONObject();
        obj.put("activityId", activityId);
        obj.put("registerId", registerId);

        TransferMessage transferMessage = new TransferMessage(idGenerator.nextId(), type,
                userId, null, null, message, JSON.toJSONString(obj));

        sendMessage(transferMessage, "send_register");
    }

    // 活动所有者 -> 报名人
    public void replyRegisterMessage(Integer type, String message, String registerId) {
        JSONObject obj = new JSONObject();
        obj.put("registerId", registerId);

        TransferMessage transferMessage = new TransferMessage(idGenerator.nextId(), type,
                null, null, null, message, JSON.toJSONString(obj));

        sendMessage(transferMessage, "reply_register");
    }

    public void sendMessage(TransferMessage message, String topic) {
        // 发送
        log.info("send message to kafka, topic: {}, message: {}", topic, message.toString());
        kafkaTemplate.send(topic, JSON.toJSONString(message));
    }

}
