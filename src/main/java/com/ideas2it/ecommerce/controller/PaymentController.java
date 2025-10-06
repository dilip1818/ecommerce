package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final OrderService orderService;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/{orderId}")
    public ResponseEntity<OrderDtos.PaymentResponse> pay(@PathVariable Integer orderId, @Valid @RequestBody OrderDtos.PaymentRequest req) {
        com.ideas2it.ecommerce.entity.Payment p = new com.ideas2it.ecommerce.entity.Payment();
        p.setAmount(req.amount);
        p.setMethod(req.method);
        return ResponseEntity.ok(orderService.pay(orderId, p));
    }
}


