package com.zhengwei.rocket.listner;

import com.alibaba.fastjson.JSONObject;
import com.zhengwei.rocket.entity.Product;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import com.zhengwei.rocket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能：本地事务监听器
 * 描述：mq收到事务消息后的应答回调以及mq回调检查本地事务执行情况
 * Component：让监听器成为容器中的一个组件
 * RocketMQTransactionListener：监听器，监听某个组的生产者发送消息后的回调
 *
 */
@Component
@RocketMQTransactionListener(txProducerGroup = "produce_pay_group")
@Slf4j
public class ProduceMqMsgListner implements RocketMQLocalTransactionListener {

    @Autowired
    ProductService productService;

    /**
     * 步骤二：
     * 描述：mq收到事务消息后，开始执行本地事务
     * 1：修改商品库存
     * 2：保存订单商品扣除记录
     * @param message 消息体
     * @param o
     * @return 事务消息进行回滚还是提交
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("executeLocalTransaction");
        try {
            String jsonString = new String((byte[])message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            ProductOrderNoLog productOrderNoLog = JSONObject.parseObject(jsonObject.getString("productOrder"), ProductOrderNoLog.class);
            Boolean result = productService.commitProduct(productOrderNoLog);
            if (result) {
                log.info("executeLocalTransaction COMMIT");
                return RocketMQLocalTransactionState.UNKNOWN;
            }else{
                log.info("executeLocalTransaction UNKNOWN");
                return RocketMQLocalTransactionState.UNKNOWN;
            }
        } catch (Exception e){
            log.info("executeLocalTransaction Exception UNKNOWN");
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }

    /**
     * 步骤三
     * 描述：mq回调检查本地事务执行情况(查询订单商品扣除记录是否存在)
     * @param message 消息体（保存里订单商品扣除）
     * @return 事务消息进行回滚还是提交
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("checkLocalTransaction");
        String jsonString = new String((byte[])message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        ProductOrderNoLog productOrder = JSONObject.parseObject(jsonObject.getString("productOrder"), ProductOrderNoLog.class);
        ProductOrderNoLog productOrderNoLog = productService.getByOrderNo(productOrder.getOrderNo());
        if(null == productOrderNoLog){
            log.info("checkLocalTransaction UNKNOWN");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        log.info("checkLocalTransaction COMMIT");
        return RocketMQLocalTransactionState.COMMIT;
    }
}
