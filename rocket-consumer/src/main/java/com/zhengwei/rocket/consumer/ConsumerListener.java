package com.zhengwei.rocket.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${rocketmq.topic}", selectorExpression = "*", consumerGroup = "${rocketmq.producer.group}")
public class ConsumerListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info(s);
    }
}
