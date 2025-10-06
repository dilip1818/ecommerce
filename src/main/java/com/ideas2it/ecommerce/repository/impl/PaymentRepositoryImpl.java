package com.ideas2it.ecommerce.repository.impl;

import com.ideas2it.ecommerce.entity.Payment;
import com.ideas2it.ecommerce.repository.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Payment> mapper = (rs, rowNum) -> {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setOrderId(rs.getInt("order_id"));
        p.setPaymentDate(rs.getTimestamp("payment_date").toInstant());
        p.setAmount(rs.getBigDecimal("amount"));
        p.setMethod(rs.getString("method"));
        return p;
    };

    public PaymentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getPaymentId() == null) {
            Integer generatedId = jdbcTemplate.queryForObject(
                    "INSERT INTO payment (order_id, payment_date, amount, method) VALUES (?, ?, ?, ?) RETURNING payment_id",
                    Integer.class,
                    payment.getOrderId(),
                    java.sql.Timestamp.from(payment.getPaymentDate()),
                    payment.getAmount(),
                    payment.getMethod()
            );

            payment.setPaymentId(generatedId);
            return payment;
        } else {
            jdbcTemplate.update(
                    "UPDATE payment SET order_id = ?, payment_date = ?, amount = ?, method = ? WHERE payment_id = ?",
                    payment.getOrderId(),
                    java.sql.Timestamp.from(payment.getPaymentDate()),
                    payment.getAmount(),
                    payment.getMethod(),
                    payment.getPaymentId()
            );
            return payment;
        }
    }


    @Override
    public Optional<Payment> findByOrderId(Integer orderId) {
        return jdbcTemplate.query("select payment_id, order_id, payment_date, amount, method from payment where order_id=?", mapper, orderId)
                .stream().findFirst();
    }
}



