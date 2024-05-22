package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;

import com.sp.service.CardService;
import com.sp.service.UserService;
import com.sp.repo.CardRepository;
import com.sp.model.Card;
import com.sp.model.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    CardService cardService;

    @Autowired
    UserService userService;

    // a verif
    // @GetMapping
    // public String listCards(@RequestParam(value = "search", required = false)
    // String search, Model model) {
    // List<Card> cards = cardRepository.findAll();
    // if (search != null && !search.isEmpty()) {
    // cards = cards.stream()
    // .filter(card -> card.getName().toLowerCase().contains(search.toLowerCase()))
    // .collect(Collectors.toList());
    // }
    // model.addAttribute("cards", cards);
    // return "index";
    // }

    @GetMapping("/add")
    public Card showAddForm() {
        return cardService.addCardDefault();
    }

    // a verif
    // @PostMapping("/add")
    // public String addCard(Card card, Model model) {
    // cardRepository.save(card);
    // return "redirect:/";
    // }

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
            user.setMoney(user.getMoney() + card.getPrice());
            return ResponseEntity.ok("Card sold successfully.");
        }
        return ResponseEntity.ok("Card sold successfully.");
    }
}
