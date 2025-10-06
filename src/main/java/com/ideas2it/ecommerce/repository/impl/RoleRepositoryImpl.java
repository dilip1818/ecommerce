package com.ideas2it.ecommerce.repository.impl;

import com.ideas2it.ecommerce.entity.Role;
import com.ideas2it.ecommerce.repository.RoleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Role> mapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleName(rs.getString("role_name"));
        return role;
    };

    public RoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Role> findById(Integer roleId) {
        return jdbcTemplate.query("select role_id, role_name from role where role_id = ?", mapper, roleId)
                .stream().findFirst();
    }

    @Override
    public Optional<Role> findByName(String roleName) {
        return jdbcTemplate.query("select role_id, role_name from role where role_name = ?", mapper, roleName)
                .stream().findFirst();
    }
}



