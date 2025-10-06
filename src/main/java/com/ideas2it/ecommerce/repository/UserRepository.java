package com.ideas2it.ecommerce.repository;

import com.ideas2it.ecommerce.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Integer userId);
    Optional<User> findByEmail(String email);
    User save(User user);
    List<User> findAll();
}



