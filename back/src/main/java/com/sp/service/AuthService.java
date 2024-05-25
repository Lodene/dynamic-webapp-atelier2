package com.sp.service;

import com.sp.utils.JwtUtil;
import com.sp.model.User;
import com.sp.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public String login(String username, String password) {
        System.out.println("username: " + username);
        User user = userRepository.findByUsername(username).orElse(null);
        System.out.println(user);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return jwtUtil.generateToken(username);
    }
}
