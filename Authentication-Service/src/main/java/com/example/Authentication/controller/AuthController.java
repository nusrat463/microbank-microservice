package com.example.Authentication.controller;

import com.example.Authentication.dto.UserProfileRequest;
import com.example.Authentication.entity.AuthRequest;
import com.example.Authentication.entity.User;
import com.example.Authentication.repository.UserRepository;
import com.example.Authentication.service.AuditService;
import com.example.Authentication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

        // Prepare DTO and send to user-service
        UserProfileRequest profile = new UserProfileRequest();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setRole(user.getRole());

        userServiceClient.createUserProfile(profile);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        System.out.println("Incoming password: " + user.getPassword());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole("ADMIN");
        userRepository.save(user);

        // Prepare DTO and send to user-service
        UserProfileRequest profile = new UserProfileRequest();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setRole(user.getRole());

        userServiceClient.createUserProfile(profile);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin registered");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate user
            authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            User user = userRepository.findByUsername(authRequest.getUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // Automatically assign ADMIN role if criteria match
            if (isAdmin(user) && !"ADMIN".equals(user.getRole())) {
                user.setRole("ADMIN");
            }

            // Generate tokens
            String accessToken = jwtUtil.generateToken(user.getUsername(),user.getId(), user.getEmail(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(),user.getId(), user.getEmail(), user.getRole());

            // Save refresh token
            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days
            userRepository.save(user);
            auditService.log(user.getUsername(), "LOGIN", "SUCCESS", "User logged in successfully");
            // Send both tokens in response
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("role", user.getRole());

            return ResponseEntity.ok(tokens);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }



    private boolean isAdmin(User user) {
        // Example criteria: Check if the user's email is in a predefined list of admins
        List<String> adminEmails = Arrays.asList("admin1@example.com", "admin2@example.com");

        return adminEmails.contains(user.getEmail());
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        String username;
        try {
            username = jwtUtil.extractUsername(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username);
        if (user == null || !refreshToken.equals(user.getRefreshToken())) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        if (user.getRefreshTokenExpiry().before(new Date())) {
            return ResponseEntity.status(403).body("Refresh token expired");
        }

        String newAccessToken = jwtUtil.generateToken(user.getUsername(),user.getId(), user.getEmail(), user.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }

}

