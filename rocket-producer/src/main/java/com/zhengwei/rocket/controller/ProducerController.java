package com.zhengwei.rocket.controller;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息生产者
 *
 * @author zhengwei 卓望信息(北京)
 * @version 1.0
 * @since 1.0
 * 2019/5/22 11:45
 **/
@RestController
public class ProducerController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/producer/{name}")
    public String producer(@PathVariable("name")  String name) {
        rocketMQTemplate.convertAndSend("producer_tag_dev", name);
        return "success";
    }

}
