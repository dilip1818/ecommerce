package com.ideas2it.ecommerce.service;

import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDtos.ProductResponse create(Product product);
    Optional<ProductDtos.ProductResponse> get(Integer productId);
    List<ProductDtos.ProductResponse> list();
    void delete(Integer productId);
    void updateStock(Integer productId, Integer stock);
    List<ProductDtos.ProductResponse> listBySeller(Integer sellerId);
    ProductDtos.ProductResponse createForSeller(Product product, Integer sellerId);
    ProductDtos.ProductResponse updateForSeller(Integer productId, Product product, Integer sellerId);
    void deleteForSeller(Integer productId, Integer sellerId);
}



