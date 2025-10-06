package com.ideas2it.ecommerce.repository;

import com.ideas2it.ecommerce.entity.Role;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findById(Integer roleId);
    Optional<Role> findByName(String roleName);
}



