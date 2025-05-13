package com.jwt.implementation.controller;
import com.jwt.implementation.model.User;
import com.jwt.implementation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/profile")
    public ResponseEntity<?> createUserProfile(@RequestBody User user) {
        User saveUser = new User();
        saveUser.setId(user.getId());
        saveUser.setName(user.getName());
        saveUser.setEmail(user.getEmail());
        saveUser.setRole(user.getRole());
        userRepository.save(saveUser);
        return ResponseEntity.ok("Profile created");
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return userRepository.findById(Long.parseLong(userId))
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser) {
        User existingUser = userRepository.findByUserId(updatedUser.getUserId());
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        userRepository.save(existingUser);
        return ResponseEntity.ok("Profile updated successfully");
    }

}
