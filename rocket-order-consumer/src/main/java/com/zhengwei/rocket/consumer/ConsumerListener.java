package com.zhengwei.rocket.consumer;

import com.alibaba.fastjson.JSONObject;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import com.zhengwei.rocket.service.AccountService;
import com.zhengwei.rocket.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能：mq消息监听器
 * 描述：监听来自producer_pay_tag的topic的消息，此topic要与生产者发送的topic一致
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "producer_pay_tag",consumerGroup = "order_consumer_group")
public class ConsumerListener implements RocketMQListener<String> {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        ProductOrderNoLog productOrderNoLog = JSONObject.parseObject(jsonObject.getString("productOrder"), ProductOrderNoLog.class);
        String accountResult = accountService.buyProduct(productOrderNoLog);
        if(null != accountResult && !"".equals(accountResult) && "success".equals(accountResult)){
            String orderResult = orderService.buyProduct(productOrderNoLog);
            if(null != orderResult && !"".equals(orderResult) && "success".equals(orderResult)){
                log.info("订单完成");
            }else{
                log.error("order异常");
            }
        }else{
            log.error("account异常");
        }
    }
}
