package com.ideas2it.ecommerce.service;

import com.ideas2it.ecommerce.dto.AuthDtos;
import com.ideas2it.ecommerce.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    AuthDtos.UserResponse register(User user);
    Optional<AuthDtos.UserResponse> findByEmail(String email);
    List<AuthDtos.UserResponse> listUsers();
}



