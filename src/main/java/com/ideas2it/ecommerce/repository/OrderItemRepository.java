package com.ideas2it.ecommerce.repository;

import com.ideas2it.ecommerce.entity.OrderItem;
import java.util.List;

public interface OrderItemRepository {
    void saveAll(List<OrderItem> items);
    List<OrderItem> findByOrderId(Integer orderId);
}



