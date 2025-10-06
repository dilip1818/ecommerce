package com.ideas2it.ecommerce.repository.impl;

import com.ideas2it.ecommerce.entity.Order;
import com.ideas2it.ecommerce.repository.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> mapper = (rs, rowNum) -> {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setOrderDate(rs.getTimestamp("order_date").toInstant());
        o.setStatus(rs.getString("status"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        return o;
    };

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order save(Order order) {
        if (order.getOrderId() == null) {
            Integer generatedId = jdbcTemplate.queryForObject(
                    "INSERT INTO orders (user_id, order_date, status, total_amount) VALUES (?, ?, ?, ?) RETURNING order_id",
                    Integer.class,
                    order.getUserId(),
                    java.sql.Timestamp.from(order.getOrderDate()),
                    order.getStatus(),
                    order.getTotalAmount()
            );
            order.setOrderId(generatedId);
            return order;
        } else {
            jdbcTemplate.update(
                    "UPDATE orders SET user_id=?, order_date=?, status=?, total_amount=? WHERE order_id=?",
                    order.getUserId(),
                    java.sql.Timestamp.from(order.getOrderDate()),
                    order.getStatus(),
                    order.getTotalAmount(),
                    order.getOrderId()
            );
            return order;
        }
    }


    @Override
    public Optional<Order> findById(Integer orderId) {
        return jdbcTemplate.query("select order_id, user_id, order_date, status, total_amount from orders where order_id=?", mapper, orderId)
                .stream().findFirst();
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        return jdbcTemplate.query("select order_id, user_id, order_date, status, total_amount from orders where user_id=? order by order_id", mapper, userId);
    }

    @Override
    public BigDecimal findPriceByProductId(Integer productId) {
        return jdbcTemplate.queryForObject(
                "SELECT price FROM product WHERE product_id = ?",
                BigDecimal.class,
                productId
        );
    }
}



