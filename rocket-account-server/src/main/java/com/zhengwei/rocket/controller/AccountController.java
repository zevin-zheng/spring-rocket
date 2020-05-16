package com.zhengwei.rocket.controller;

import com.zhengwei.rocket.entity.Account;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import com.zhengwei.rocket.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * 修改用户金额
     * @param productOrderNoLog 订单商品记录
     * @return 是否修改成功
     */
    @RequestMapping("/buyProduct")
    public String buyProduct(@RequestBody ProductOrderNoLog productOrderNoLog) {
        log.info("buyProduct:{}", productOrderNoLog.getOrderNo());
        LocalDateTime now = LocalDateTime.now();
        Account account = accountService.getById(1);
        account.setSum(account.getSum() != null ? account.getSum() + 1 : 1);
        account.setLastUpdateTime(now);
        accountService.updateById(account);
        return "success";
    }
}
