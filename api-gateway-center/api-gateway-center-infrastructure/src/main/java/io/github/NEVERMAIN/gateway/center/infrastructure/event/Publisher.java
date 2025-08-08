package io.github.NEVERMAIN.gateway.center.infrastructure.event;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 消息发送者
 */
@Component
public class Publisher {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void pushMessage(String topic, Object message) {
        redisTemplate.convertAndSend(topic, message);
    }

}
