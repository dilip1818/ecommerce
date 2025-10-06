package com.ideas2it.ecommerce.repository.impl;

import com.ideas2it.ecommerce.entity.OrderItem;
import com.ideas2it.ecommerce.repository.OrderItemRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItem> mapper = (rs, rowNum) -> {
        OrderItem oi = new OrderItem();
        oi.setOrderItemId(rs.getInt("order_item_id"));
        oi.setOrderId(rs.getInt("order_id"));
        oi.setProductId(rs.getInt("product_id"));
        oi.setQuantity(rs.getInt("quantity"));
        oi.setPrice(rs.getBigDecimal("price"));
        return oi;
    };

    public OrderItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveAll(List<OrderItem> items) {
        jdbcTemplate.batchUpdate("insert into order_item(order_id, product_id, quantity, price) values(?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                        OrderItem item = items.get(i);
                        ps.setInt(1, item.getOrderId());
                        ps.setInt(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());
                        ps.setBigDecimal(4, item.getPrice());
                    }

                    @Override
                    public int getBatchSize() {
                        return items.size();
                    }
                });
    }

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) {
        return jdbcTemplate.query("select order_item_id, order_id, product_id, quantity, price from order_item where order_id=?", mapper, orderId);
    }
}



