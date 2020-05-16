package com.zhengwei.rocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhengwei.rocket.entity.Product;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import org.apache.ibatis.annotations.Param;

/**
 * TODO
 *
 * @author zevin aibaokeji
 * @version 1.0
 * 2020/4/1514:45
 **/
public interface ProductOrderNoLogMapper extends BaseMapper<ProductOrderNoLog> {

    /**
     * 根据订单号查询商品扣除记录
     * @param orderNo 订单号
     * @return 商品扣除记录
     */
    ProductOrderNoLog findByOrderNo(@Param("orderNo") String orderNo);
}
