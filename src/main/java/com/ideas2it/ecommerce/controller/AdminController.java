package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.AuthDtos;
import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.entity.Role;
import com.ideas2it.ecommerce.repository.RoleRepository;
import com.ideas2it.ecommerce.repository.UserRepository;
import com.ideas2it.ecommerce.service.OrderService;
import com.ideas2it.ecommerce.service.ProductService;
import com.ideas2it.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AdminController(UserService userService, ProductService productService, OrderService orderService, RoleRepository roleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<AuthDtos.UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> allOrders() {
        return ResponseEntity.ok(orderService.listAllOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<ProductDtos.ProductResponse> createProduct(@Valid @RequestBody ProductDtos.CreateRequest req) {
        Product p = new Product();
        p.setName(req.name);
        p.setDescription(req.description);
        p.setPrice(req.price);
        p.setStock(req.stock);
        return ResponseEntity.ok(productService.create(p));
    }
}


