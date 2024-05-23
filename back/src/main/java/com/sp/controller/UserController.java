package com.sp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sp.model.User;
import com.sp.service.CardService;
import com.sp.service.UserService;
import com.sp.utils.JwtUtil;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CardService cardService;

    @Autowired
    JwtUtil jwtUtil;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody User userParam) {
        try {
            Long userId = userService.login(userParam.getUsername(), userParam.getPassword());
            User user = userService.getUser(userId);
            final String jwt = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/create-account")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        try {
            user = userService.createUserWithInitialCards(user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/add_card_user/{id_card}/{id_user}")
    public User addCard(@PathVariable Long id_card, @PathVariable Long id_user) {
        userService.addCardtoUser(id_card, id_user);
        return userService.getUser(id_user);
    }
}

class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
