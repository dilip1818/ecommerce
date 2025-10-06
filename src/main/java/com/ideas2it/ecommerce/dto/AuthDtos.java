package com.ideas2it.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public static class RegisterRequest {
        @NotBlank
        public String name;
        @Email
        public String email;
        @NotBlank
        public String password;
        public String phone;
        public Integer roleId;
    }

    public static class LoginRequest {
        @Email
        public String email;
        @NotBlank
        public String password;
    }

    public static class UserResponse {
        public Integer userId;
        public String name;
        public String email;
        public String phone;
        public Integer roleId;
    }
}


