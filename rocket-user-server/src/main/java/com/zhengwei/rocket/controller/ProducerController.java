package com.zhengwei.rocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhengwei.rocket.entity.Product;
import com.zhengwei.rocket.service.AccountService;
import com.zhengwei.rocket.service.OrderService;
import com.zhengwei.rocket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ${description}
 *
 * @author zhengwei 卓望信息(北京)
 * @version 1.0
 * @since 1.0
 * 2019/5/22 11:45
 **/
@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    private Lock lock = new ReentrantLock();

    /**
     * 秒杀下单分布式事务测试
     * @return
     * @throws
     */
    @GetMapping(value = "testCommit")
    public Boolean testCommit(){
        lock.lock();
        try {
            Product product = productService.getById(1);
            if (product.getStock() > 0) {
                //将要发送的对象转换成消息需要的json字符串格式
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("modProduct", product);
                Message<String> message = MessageBuilder.withPayload(jsonObject.toString()).build();
                rocketMQTemplate.sendMessageInTransaction("produce_pay_group", "producer_pay_tag",message,null);
                log.info("======消息事务发送成功======");
                return true;
            } else {
                log.info("======消息事务发送失败======");
                return false;
            }
        } catch (Exception e) {
            log.info("======异常======");
            return false;
        } finally {
            lock.unlock();
            log.info("======释放锁======");
        }
    }

    /**
     * 步骤三
     * 功能：更新库存信息
     * 描述：事务消息发送成功后，mq收到消息并应答，开始执行更新库存事务
     * @param modProduct
     */
    public void commitProduct(Product modProduct){
        Product oldProduct = productService.getById(modProduct.getId());
        oldProduct.setStock(oldProduct.getStock() - 1);
        productService.updateById(oldProduct);
    }
}
