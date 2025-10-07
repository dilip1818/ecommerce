package com.ideas2it.ecommerce.repository.impl;

import com.ideas2it.ecommerce.entity.Product;
import com.ideas2it.ecommerce.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> mapper = (rs, rowNum) -> {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        try { p.setSellerId(rs.getInt("seller_id")); } catch (Exception ignored) {}
        return p;
    };

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product save(Product product) {
        if (product.getProductId() == null) {
            Integer generatedId = jdbcTemplate.queryForObject(
                    "INSERT INTO product (name, description, price, stock, seller_id) VALUES (?, ?, ?, ?, ?) RETURNING product_id",
                    Integer.class,
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getSellerId()
            );
            product.setProductId(generatedId);
            return product;
        } else {
            jdbcTemplate.update(
                    "UPDATE product SET name=?, description=?, price=?, stock=?, seller_id=? WHERE product_id=?",
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getSellerId(),
                    product.getProductId()
            );
            return product;
        }
    }


    @Override
    public Optional<Product> findById(Integer productId) {
        return jdbcTemplate.query("select product_id, name, description, price, stock, seller_id from product where product_id=?", mapper, productId)
                .stream().findFirst();
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select product_id, name, description, price, stock, seller_id from product order by product_id", mapper);
    }

    @Override
    public void deleteById(Integer productId) {
        jdbcTemplate.update("delete from product where product_id=?", productId);
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        jdbcTemplate.update("update product set stock=? where product_id=?", stock, productId);
    }

    @Override
    public List<Product> findBySellerId(Integer sellerId) {
        return jdbcTemplate.query("select product_id, name, description, price, stock, seller_id from product where seller_id=? order by product_id", mapper, sellerId);
    }
}



