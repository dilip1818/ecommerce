package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.ideas2it.ecommerce.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerController {
    private final ProductService productService;
    private final CurrentUserService currentUserService;

    public SellerController(ProductService productService, CurrentUserService currentUserService) {
        this.productService = productService;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/products")
    public ResponseEntity<ProductDtos.ProductResponse> createProduct(Authentication auth, @Valid @RequestBody ProductDtos.CreateRequest req) {
        Product p = new Product();
        p.setName(req.name);
        p.setDescription(req.description);
        p.setPrice(req.price);
        p.setStock(req.stock);
        Integer sellerId = currentUserService.getCurrentUserId(auth);
        return ResponseEntity.ok(productService.createForSeller(p, sellerId));
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/products")
    public ResponseEntity<List<ProductDtos.ProductResponse>> listMyProducts(Authentication auth) {
        Integer sellerId = currentUserService.getCurrentUserId(auth);
        return ResponseEntity.ok(productService.listBySeller(sellerId));
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDtos.ProductResponse> updateProduct(Authentication auth, @PathVariable Integer id, @Valid @RequestBody ProductDtos.CreateRequest req) {
        Integer sellerId = currentUserService.getCurrentUserId(auth);
        Product p = new Product();
        p.setName(req.name);
        p.setDescription(req.description);
        p.setPrice(req.price);
        p.setStock(req.stock);
        return ResponseEntity.ok(productService.updateForSeller(id, p, sellerId));
    }

    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(Authentication auth, @PathVariable Integer id) {
        Integer sellerId = currentUserService.getCurrentUserId(auth);
        productService.deleteForSeller(id, sellerId);
        return ResponseEntity.noContent().build();
    }
}


