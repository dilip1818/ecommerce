package com.ideas2it.ecommerce.repository;

import com.ideas2it.ecommerce.entity.Payment;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(Integer orderId);
}



