package com.fxa.image.store.service;

import com.fxa.image.store.mapper.ProductMapper;
import com.fxa.image.store.mode.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    @Autowired
    ProductMapper productMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateProductStock(long id) {
        Product product = productMapper.getProductById(id);
        if (product != null && product.getStock() > 0) {
            product.setStock(product.getStock() - 1);
            productMapper.updateProduct(product);
        }
    }

}
