package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.ideas2it.ecommerce.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public DeliveryController(OrderService orderService, CurrentUserService currentUserService) {
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("hasRole('DELIVERY_AGENT')")
    @GetMapping("/assigned-orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> myAssignedOrders(Authentication auth) {
        Integer deliveryAgentId = currentUserService.getCurrentUserId(auth);
        return ResponseEntity.ok(orderService.listAssignedOrders(deliveryAgentId));
    }

    @PreAuthorize("hasRole('DELIVERY_AGENT')")
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<OrderDtos.OrderResponse> updateStatus(Authentication auth, @PathVariable Integer orderId, @RequestParam String status) {
        Integer deliveryAgentId = currentUserService.getCurrentUserId(auth);
        return ResponseEntity.ok(orderService.updateDeliveryStatus(orderId, status, deliveryAgentId));
    }
}


