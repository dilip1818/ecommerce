package com.ideas2it.ecommerce.service;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.entity.OrderItem;
import com.ideas2it.ecommerce.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderDtos.OrderResponse createOrder(Integer userId, List<OrderItem> items);
    OrderDtos.PaymentResponse pay(Integer orderId);
    List<OrderDtos.OrderResponse> listUserOrders(Integer userId);
    BigDecimal getPriceByProductId(Integer productId);
    List<OrderDtos.OrderResponse> listAllOrders();
    List<OrderDtos.OrderResponse> listAssignedOrders(Integer deliveryAgentId);
    OrderDtos.OrderResponse updateDeliveryStatus(Integer orderId, String status, Integer deliveryAgentId);
}



