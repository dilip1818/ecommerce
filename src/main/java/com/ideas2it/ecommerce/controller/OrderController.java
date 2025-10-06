package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.entity.OrderItem;
import com.ideas2it.ecommerce.entity.Payment;
import com.ideas2it.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDtos.OrderResponse> create(@Valid @RequestBody OrderDtos.CreateOrderRequest req) {
        List<OrderItem> items = req.items.stream().map(r -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(r.productId);
            orderItem.setQuantity(r.quantity);
            orderItem.setPrice(orderService.getPriceByProductId(r.productId));
            return orderItem;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(orderService.createOrder(req.userId, items));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderDtos.PaymentResponse> pay(@PathVariable Integer orderId, @Valid @RequestBody OrderDtos.PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.amount);
        payment.setMethod(paymentRequest.method);
        return ResponseEntity.ok(orderService.pay(orderId, payment));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDtos.OrderResponse>> list(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.listUserOrders(userId));
    }
}


