package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.dto.ProductDtos;
import com.ideas2it.ecommerce.entity.OrderItem;
import com.ideas2it.ecommerce.entity.Payment;
import com.ideas2it.ecommerce.service.OrderService;
import com.ideas2it.ecommerce.service.CurrentUserService;
import com.ideas2it.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserRoleController {
    private final ProductService productService;
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public UserRoleController(ProductService productService, OrderService orderService, CurrentUserService currentUserService) {
        this.productService = productService;
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/products")
    public ResponseEntity<List<ProductDtos.ProductResponse>> listProducts() {
        return ResponseEntity.ok(productService.list());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/orders")
    public ResponseEntity<OrderDtos.OrderResponse> createOrder(Authentication auth, @Valid @RequestBody OrderDtos.CreateOrderRequest req) {
        List<OrderItem> items = req.items.stream().map(r -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(r.productId);
            orderItem.setQuantity(r.quantity);
            orderItem.setPrice(orderService.getPriceByProductId(r.productId));
            return orderItem;
        }).collect(Collectors.toList());
        Integer userId = currentUserService.getCurrentUserId(auth);
        return ResponseEntity.ok(orderService.createOrder(userId, items));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> myOrders(Authentication auth) {
        Integer userId = currentUserService.getCurrentUserId(auth);
        return ResponseEntity.ok(orderService.listUserOrders(userId));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/payment")
    public ResponseEntity<OrderDtos.PaymentResponse> pay(@RequestParam Integer orderId) {
        return ResponseEntity.ok(orderService.pay(orderId));
    }
}


