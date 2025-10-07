package com.ideas2it.ecommerce.repository;

import com.ideas2it.ecommerce.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Integer productId);
    List<Product> findAll();
    void deleteById(Integer productId);
    void updateStock(Integer productId, Integer stock);
    List<Product> findBySellerId(Integer sellerId);
}



