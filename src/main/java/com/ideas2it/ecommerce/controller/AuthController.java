package com.ideas2it.ecommerce.controller;

import com.ideas2it.ecommerce.dto.AuthDtos;
import com.ideas2it.ecommerce.entity.User;
import com.ideas2it.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.UserResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(request.password);
        user.setPhone(request.phone);
        user.setRoleId(request.roleId);
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        return ResponseEntity.ok(authentication != null ? authentication.getName() : "anonymous");
    }
}


