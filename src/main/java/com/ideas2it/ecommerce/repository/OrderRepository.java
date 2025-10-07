package com.ideas2it.ecommerce.repository;

import com.ideas2it.ecommerce.entity.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Integer orderId);
    List<Order> findByUserId(Integer userId);
    BigDecimal findPriceByProductId(Integer productId);
    List<Order> findByDeliveryAgentId(Integer deliveryAgentId);
    List<Order> findAll();
    void updateDeliveryStatus(Integer orderId, String status, Integer deliveryAgentId);
}



