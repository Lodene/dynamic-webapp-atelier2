package com.sp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sp.model.User;
import com.sp.repo.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestUtil {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public Long getUserId(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
            return user.getId();
        } else {
            throw new IllegalArgumentException("Authorization header is missing or not well-formed");
        }
    }
}
