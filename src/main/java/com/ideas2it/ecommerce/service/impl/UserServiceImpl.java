package com.ideas2it.ecommerce.service.impl;

import com.ideas2it.ecommerce.dto.AuthDtos;
import com.ideas2it.ecommerce.entity.User;
import com.ideas2it.ecommerce.exception.ResourceAlreadyExistsException;
import com.ideas2it.ecommerce.exception.ResourceNotFoundException;
import com.ideas2it.ecommerce.repository.UserRepository;
import com.ideas2it.ecommerce.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthDtos.UserResponse register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("User", "email", user.getEmail());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return modelMapper.map(saved, AuthDtos.UserResponse.class);
    }

    @Override
    public Optional<AuthDtos.UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(u -> modelMapper.map(u, AuthDtos.UserResponse.class));
    }

    @Override
    public List<AuthDtos.UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, AuthDtos.UserResponse.class))
                .collect(Collectors.toList());
    }
}



