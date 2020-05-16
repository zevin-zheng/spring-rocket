package com.zhengwei.rocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhengwei.rocket.entity.Product;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import com.zhengwei.rocket.mapper.ProductOrderNoLogMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author zevin aibaokeji
 * @version 1.0
 * 2020/4/1514:45
 **/
public interface ProductService extends IService<Product> {

    /**
     * 根据订单号查询商品扣除记录
     * @param orderNo 订单号
     * @return 商品扣除记录
     */
    ProductOrderNoLog getByOrderNo(String orderNo);

    /**
     * 提交订单
     * @param productOrderNoLog 商品订单记录
     * @return 是否提交成功
     */
    Boolean commitProduct(ProductOrderNoLog productOrderNoLog);

    /**
     * 回滚订单
     * @param productOrderNoLog 商品订单记录
     * @return 是否回退成功
     */
    Boolean roBackProduct(ProductOrderNoLog productOrderNoLog);
}

