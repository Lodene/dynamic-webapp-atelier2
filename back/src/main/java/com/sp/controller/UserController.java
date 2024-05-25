package com.sp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.service.CardService;
import com.sp.service.UserService;
import com.sp.utils.JwtUtil;
import com.sp.utils.RequestUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CardService cardService;
    
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RequestUtil requestUtil;

    @RequestMapping(method = RequestMethod.PUT, value = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        try {
            user = userService.createUserWithInitialCards(user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(HttpServletRequest request) {
		Long userId = requestUtil.getUserId(request);
        System.out.println("userId: " + userId);
        try {
            User user = userService.getUser(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

	}
    
    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getUserCards(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Claims claims = Jwts.parser().setSigningKey("your_secret_key").parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        System.out.println(username);
        List<Card> cards = cardService.getCardsByUsername(username);
        return ResponseEntity.ok(cards);
    }
}

