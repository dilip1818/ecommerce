package com.ideas2it.ecommerce.service.impl;

import com.ideas2it.ecommerce.dto.OrderDtos;
import com.ideas2it.ecommerce.entity.Order;
import com.ideas2it.ecommerce.entity.OrderItem;
import com.ideas2it.ecommerce.entity.Payment;
import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.exception.InsufficientStockException;
import com.ideas2it.ecommerce.exception.InvalidOrderException;
import com.ideas2it.ecommerce.exception.PaymentException;
import com.ideas2it.ecommerce.exception.ResourceNotFoundException;
import com.ideas2it.ecommerce.repository.OrderItemRepository;
import com.ideas2it.ecommerce.repository.OrderRepository;
import com.ideas2it.ecommerce.repository.PaymentRepository;
import com.ideas2it.ecommerce.repository.ProductRepository;
import com.ideas2it.ecommerce.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("Order items cannot be empty");
        }

        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", item.getProductId()));
            
            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(product.getName(), item.getQuantity(), product.getStock());
            }
        }

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

        for (OrderItem item : items) {
            productRepository.updateStock(item.getProductId(), 
                productRepository.findById(item.getProductId()).get().getStock() - item.getQuantity());
        }

        return modelMapper.map(order, OrderDtos.OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderDtos.PaymentResponse pay(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new PaymentException(orderId.toString(), "Order is already paid");
        }
        Payment payment = new Payment();
        payment.setAmount(order.getTotalAmount());
        
        payment.setOrderId(orderId);
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(Instant.now());
        }
        Payment saved = paymentRepository.save(payment);
        order.setStatus("PAID");
        orderRepository.save(order);
        
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

    @Override
    public List<OrderDtos.OrderResponse> listAllOrders() {
        return orderRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDtos.OrderResponse.class))
                .toList();
    }

    @Override
    public List<OrderDtos.OrderResponse> listAssignedOrders(Integer deliveryAgentId) {
        return orderRepository.findByDeliveryAgentId(deliveryAgentId).stream()
                .map(o -> modelMapper.map(o, OrderDtos.OrderResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public OrderDtos.OrderResponse updateDeliveryStatus(Integer orderId, String status, Integer deliveryAgentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new com.ideas2it.ecommerce.exception.ResourceNotFoundException("Order", "id", orderId));
        orderRepository.updateDeliveryStatus(orderId, status, deliveryAgentId);
        order.setStatus(status);
        order.setDeliveryAgentId(deliveryAgentId);
        return modelMapper.map(order, OrderDtos.OrderResponse.class);
    }
}


