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
    @PostMapping("/add")
    public Card addCard(Card card) {
        cardService.addCard(card);
        return card;
    }

}
