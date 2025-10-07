package com.ideas2it.ecommerce.repository.impl;

import com.ideas2it.ecommerce.entity.User;
import com.ideas2it.ecommerce.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> mapper = (rs, rowNum) -> {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setRoleId(rs.getInt("role_id"));
        return u;
    };

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return jdbcTemplate.query("select user_id, name, email, password, phone, role_id from users where user_id = ?", mapper, userId)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("select user_id, name, email, password, phone, role_id from users where email = ?", mapper, email)
                .stream().findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            String sql = """
            INSERT INTO users (name, email, password, phone, role_id)
            VALUES (?, ?, ?, ?, ?) RETURNING user_id""";

            Integer generatedId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhone(),
                    user.getRoleId()
            );

            user.setUserId(generatedId);
            return user;

        } else {
            String sql = """
            UPDATE users
            SET name = ?, email = ?, password = ?, phone = ?, role_id = ?
            WHERE user_id = ?
            """;

            int rowsUpdated = jdbcTemplate.update(
                    sql,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhone(),
                    user.getRoleId(),
                    user.getUserId()
            );

            if (rowsUpdated == 0) {
                throw new RuntimeException("User not found with id: " + user.getUserId());
            }
            return user;
        }
    }


    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select user_id, name, email, password, phone, role_id from users order by user_id", mapper);
    }

    @Override
    public void deleteById(Integer userId) {
        jdbcTemplate.update("delete from users where user_id=?", userId);
    }
}



