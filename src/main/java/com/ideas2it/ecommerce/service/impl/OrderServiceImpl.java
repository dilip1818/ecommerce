package com.ideas2it.ecommerce.service.impl;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.entity.Order;
import com.ideas2it.ecommerce.entity.OrderItem;
import com.ideas2it.ecommerce.entity.Payment;
import com.ideas2it.ecommerce.repository.OrderItemRepository;
import com.ideas2it.ecommerce.repository.OrderRepository;
import com.ideas2it.ecommerce.repository.PaymentRepository;
import com.ideas2it.ecommerce.repository.ProductRepository;
import com.ideas2it.ecommerce.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, PaymentRepository paymentRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderDtos.OrderResponse createOrder(Integer userId, List<OrderItem> items) {
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(Instant.now());
        order.setStatus("CREATED");
        order.setTotalAmount(total);
        order = orderRepository.save(order);

        Integer orderId = order.getOrderId();
        for (OrderItem item : items) {
            item.setOrderId(orderId);
        }
        orderItemRepository.saveAll(items);

        return modelMapper.map(order, OrderDtos.OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderDtos.PaymentResponse pay(Integer orderId, Payment payment) {
        payment.setOrderId(orderId);
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(Instant.now());
        }
        Payment saved = paymentRepository.save(payment);
        return modelMapper.map(saved, OrderDtos.PaymentResponse.class);
    }

    @Override
    public List<OrderDtos.OrderResponse> listUserOrders(Integer userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(o -> modelMapper.map(o, OrderDtos.OrderResponse.class))
                .toList();
    }

    @Override
    public BigDecimal getPriceByProductId(Integer productId) {
        return orderRepository.findPriceByProductId(productId);
    }
}


