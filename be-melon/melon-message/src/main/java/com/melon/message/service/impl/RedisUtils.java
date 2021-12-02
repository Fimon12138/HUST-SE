package com.melon.message.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RedisUtils {

    // 从 kafka 中拦截消息

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 将待撤回消息的 id 放到 redis 中
    public void withdrawMessage(String messageId) {
        try {
            stringRedisTemplate.opsForValue().set(messageId, "withdraw");
            log.info("send withdraw command of messsage {} to redis successfully", messageId);
        } catch (Exception e) {
            log.error("redis error: can't send withdraw command of message {}", messageId);
        }
    }

    // 查询 redis 看是否有这条消息的撤回记录
    public boolean isWithdraw(String messageId) {
        try {
            String str = stringRedisTemplate.opsForValue().get(messageId);
            if (!StringUtils.isEmpty(str)) {
                // 有记录，跳过处理该条消息
                log.info("get withdraw command of message {} from redis", messageId);
                return true;
            }
        } catch (Exception e) {
            log.error("redis error: can't get withdraw status of message {}", messageId);
        }
        return false;
    }

    // 删除撤回记录
    public void delWithdraw(String messageId) {
        try {
            stringRedisTemplate.delete(messageId);
            log.info("delete withdraw command of message {} from redis", messageId);
        } catch (Exception e) {
            log.error("redis error: can't delete withdraw command of message {} from redis", messageId);
        }
    }

}
