package com.zhengwei.rocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengwei.rocket.entity.Product;
import com.zhengwei.rocket.entity.ProductOrderNoLog;
import com.zhengwei.rocket.mapper.ProductMapper;
import com.zhengwei.rocket.mapper.ProductOrderNoLogMapper;
import com.zhengwei.rocket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 *
 * @author zevin aibaokeji
 * @version 1.0
 * 2020/4/1514:45
 **/
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductOrderNoLogMapper productOrderNoLogMapper;

    @Override
    public ProductOrderNoLog getByOrderNo(String orderNo) {
        return productOrderNoLogMapper.findByOrderNo(orderNo);
    }

    @Override
    @Transactional
    public Boolean commitProduct(ProductOrderNoLog productOrderNoLog) {
        Product product = getById(productOrderNoLog.getProductId());
        if (product.getStock() > 0) {
            product.setStock(product.getStock() - productOrderNoLog.getTotalAmount());
            updateById(product);
            productOrderNoLogMapper.insert(productOrderNoLog);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean roBackProduct(ProductOrderNoLog productOrder) {
        Product product = getById(productOrder.getProductId());
        if (null != product) {
            product.setStock(product.getStock() +  productOrder.getTotalAmount());
            updateById(product);
            ProductOrderNoLog productOrderNoLog =  productOrderNoLogMapper.findByOrderNo(productOrder.getOrderNo());
            productOrderNoLogMapper.deleteById(productOrderNoLog.getId());
            return true;
        }
        return false;
    }
}
