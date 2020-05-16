package com.zhengwei.rocket.service;

import com.zhengwei.rocket.entity.Orders;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 *
 * @author zevin aibaokeji
 * @version 1.0
 * 2020/4/1514:15
 **/
@FeignClient(value = "rocket-order-server")
public interface OrderService{

    @RequestMapping(value = "/buyProduct")
    String buyProduct(@RequestBody ProductOrderNoLog productOrderNoLog);
}
