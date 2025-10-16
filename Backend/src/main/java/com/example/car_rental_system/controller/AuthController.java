package com.example.car_rental_system.controller;

import com.example.car_rental_system.model.User;
import com.example.car_rental_system.repository.UserRepository;
import com.example.car_rental_system.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>(
                    Collections.singletonMap("error", "❌ Username already taken!"),
                    HttpStatus.CONFLICT
            );
        }

        if(user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>(
                Collections.singletonMap("message", "✅ User registered successfully!"),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);
        
        // Get user details
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("token", token);
        if (user != null) {
            Map<String, Object> userDetails = new java.util.HashMap<>();
            userDetails.put("username", user.getUsername());
            userDetails.put("email", user.getEmail());
            userDetails.put("firstName", user.getFirstName());
            userDetails.put("lastName", user.getLastName());
            userDetails.put("phone", user.getPhone());
            userDetails.put("role", user.getRole());
            response.put("user", userDetails);
        }
        
        return ResponseEntity.ok(response);
    }
}