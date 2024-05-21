package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sp.repo.UserRepository;
import com.sp.repo.CardRepository;
import com.sp.model.User;
import com.sp.model.Card;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/create-account")
    public ResponseEntity<String> createUser(User user) {
        try {
            List<Card> allCard = new ArrayList<Card>();
            List<Integer> allId = new ArrayList<Integer>();
            allCard = cardRepository.findAll();
            int lastId = allCard.size();
            while (allId.size() < 6) {
                int random = (int) (Math.random() * lastId);
                if (!allId.contains(random)) {
                    allId.add(random);
                }
            }
            for (int id : allId) {
                Card card = allCard.get(id);
                user.addCard(card);
            }
            userRepo.save(user);
            return new ResponseEntity<>("Account created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Account creation failed: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<User> userData = userRepo.findByUsername(username);
            if (userData.isPresent()) {
                User user = userData.get();
                if (user.getPassword().equals(password)) {
                    return ResponseEntity.ok("Login successful.");
                } else {
                    return new ResponseEntity<>("Invalid password.", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/delete-account")
    public ResponseEntity<String> deleteUser(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<User> userData = userRepo.findByUsername(username);
            if (userData.isPresent()) {
                User user = userData.get();
                if (user.getPassword().equals(password)) {
                    userRepo.delete(user);
                    return ResponseEntity.ok("Account deleted successfully.");
                } else {
                    return new ResponseEntity<>("Invalid password.", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Account deletion failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
