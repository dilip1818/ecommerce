package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDtos.ProductResponse> create(@Valid @RequestBody ProductDtos.CreateRequest req) {
        Product product = new Product();
        product.setName(req.name);
        product.setDescription(req.description);
        product.setPrice(req.price);
        product.setStock(req.stock);
        ProductDtos.ProductResponse saved = productService.create(product);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ProductDtos.ProductResponse>> list() {
        return ResponseEntity.ok(productService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDtos.ProductResponse> get(@PathVariable Integer id) {
        return productService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


