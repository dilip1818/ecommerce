package com.ideas2it.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class OrderDtos {
    public static class OrderItemRequest {
        @NotNull public Integer productId;
        @NotNull @Min(1) public Integer quantity;
    }

    public static class CreateOrderRequest {
        @NotNull public List<OrderItemRequest> items;
    }

    public static class PaymentRequest {
        @NotNull public BigDecimal amount;
        @NotNull public String method;
    }

    public static class OrderResponse {
        public Integer orderId;
        public Integer userId;
        public java.time.Instant orderDate;
        public String status;
        public BigDecimal totalAmount;
        public Integer deliveryAgentId;
    }

    public static class PaymentResponse {
        public Integer paymentId;
        public Integer orderId;
        public java.time.Instant paymentDate;
        public BigDecimal amount;
        public String method;
    }
}


