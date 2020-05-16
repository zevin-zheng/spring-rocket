package com.zhengwei.rocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhengwei.rocket.entity.Product;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import com.zhengwei.rocket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 步骤一：
     * 描述：向mq发送事务消息
     * @param id 商品ID
     * @return 事务消息进行回滚还是提交
     */
    @RequestMapping("/buyProduct")
    public String buyProduct(@RequestParam(name = "id") Integer id) {
        log.info("buyProduct:{}", id);
        Product product = productService.getById(id);
        if (product.getStock() > 0) {
            //将要发送的对象转换成消息需要的json字符串格式
            JSONObject jsonObject = new JSONObject();
            ProductOrderNoLog productOrderNoLog = new ProductOrderNoLog();
            String orderNo = UUID.randomUUID().toString().replaceAll("-","");
            productOrderNoLog.setOrderNo(orderNo);
            productOrderNoLog.setProductId(product.getId());
            productOrderNoLog.setTotalAmount(1);
            productOrderNoLog.setAmount(product.getPrice());
            productOrderNoLog.setAccountId(1);
            jsonObject.put("productOrder", productOrderNoLog);
            Message<String> message = MessageBuilder.withPayload(jsonObject.toString()).build();
            rocketMQTemplate.sendMessageInTransaction("produce_pay_group", "producer_pay_tag",message,null);
            log.info("======消息事务发送成功======");
            return "购买成功";
        } else {
            log.info("======消息事务发送失败======");
            return "库存不足";
        }
    }

    /**
     * 步骤四
     * 功能：消费者异常mq回调此接口手动补偿数据
     * @param productOrderNoLog 订单商品记录
     */
    @RequestMapping("/roBackProduct")
    public Boolean roBackProduct(@RequestBody ProductOrderNoLog productOrderNoLog){
        log.info("roBackProduct:{}",productOrderNoLog.getOrderNo());
        return productService.roBackProduct(productOrderNoLog);
    }
}