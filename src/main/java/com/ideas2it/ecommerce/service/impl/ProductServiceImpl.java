package com.ideas2it.ecommerce.service.impl;

import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.repository.ProductRepository;
import com.ideas2it.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDtos.ProductResponse create(Product product) {
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDtos.ProductResponse.class);
    }

    @Override
    public Optional<ProductDtos.ProductResponse> get(Integer productId) {
        return productRepository.findById(productId)
                .map(p -> modelMapper.map(p, ProductDtos.ProductResponse.class));
    }

    @Override
    public List<ProductDtos.ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductDtos.ProductResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        productRepository.updateStock(productId, stock);
    }
}



