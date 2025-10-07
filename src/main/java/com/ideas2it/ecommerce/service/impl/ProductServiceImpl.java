package com.ideas2it.ecommerce.service.impl;

import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.exception.InvalidOperationException;
import com.ideas2it.ecommerce.exception.ResourceNotFoundException;
import com.ideas2it.ecommerce.repository.ProductRepository;
import com.ideas2it.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!productRepository.findById(productId).isPresent()) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        productRepository.updateStock(productId, stock);
    }

    @Override
    public List<ProductDtos.ProductResponse> listBySeller(Integer sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(p -> modelMapper.map(p, ProductDtos.ProductResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDtos.ProductResponse createForSeller(Product product, Integer sellerId) {
        product.setSellerId(sellerId);
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductDtos.ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductDtos.ProductResponse updateForSeller(Integer productId, Product product, Integer sellerId) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        if (existing.getSellerId() == null || !existing.getSellerId().equals(sellerId)) {
            throw new InvalidOperationException("You can update only your own products");
        }
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setSellerId(sellerId);
        Product saved = productRepository.save(existing);
        return modelMapper.map(saved, ProductDtos.ProductResponse.class);
    }

    @Override
    @Transactional
    public void deleteForSeller(Integer productId, Integer sellerId) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        if (existing.getSellerId() == null || !existing.getSellerId().equals(sellerId)) {
            throw new InvalidOperationException("You can delete only your own products");
        }
        productRepository.deleteById(productId);
    }
}



