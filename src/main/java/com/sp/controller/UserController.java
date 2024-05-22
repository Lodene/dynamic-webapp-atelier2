package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sp.service.CardService;
import com.sp.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

import com.sp.model.User;
import com.sp.model.Card;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CardService cardService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public User login(@RequestBody User userParam)
            throws IOException {
        Long userId = userService.login(userParam.getUsername(), userParam.getPassword());
        User user = userService.getUser(userId);
        return user;
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

    // a verif
    @GetMapping("/buy/{id_card}/{id_user}")
    public User buyCard(@PathVariable Long id_card, @PathVariable Long id_user) {
        System.out.println("id_card: " + id_card);
        System.out.println("id_user: " + id_user);
        userService.buyCard(id_card, id_user);
        User user = userService.getUser(id_user);
        return user;
    }

    // a verif
    @GetMapping("/sell/{id_card}/{id_user}")
    public ResponseEntity<String> sellCard(Long id_card, Long id_user) {
        Card card = cardService.findById(id_card);
        User user = userService.getUser(id_user);
        if (card != null) {
            if (!user.getCards().contains(card)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't own this card.");
            }
            user.removeCard(card);
            card.removeUser();
            user.setMoney(user.getMoney() + card.getPrice());
            return ResponseEntity.ok("Card sold successfully.");
        }
        return ResponseEntity.ok("Card sold successfully.");
    }
}