package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;

import com.sp.repo.CardRepository;
import com.sp.model.Card;
import com.sp.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @GetMapping
    public String listCards(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Card> cards = cardRepository.findAll();
        if (search != null && !search.isEmpty()) {
            cards = cards.stream()
                    .filter(card -> card.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("cards", cards);
        return "index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Card card = new Card();
        // Initialiser avec des valeurs par défaut
        card.setName("Une carte");
        card.setDescription("Une description");
        card.setImgUrl("https://meme-gag.com/wp-content/uploads/2017/12/meme-14723.jpg");
        card.setHp(100);
        card.setEnergy(50);
        card.setAttack(75);
        card.setDefense(75);
        card.setFamily("Une famille");
        card.setAffinity("Une affinité");
        card.setPrice(100);

        model.addAttribute("card", card);
        return "addCardForm";
    }

    @PostMapping("/add")
    public String addCard(Card card, Model model) {
        cardRepository.save(card);
        return "redirect:/";
    }

    @GetMapping("/buy/{id}")
    public ResponseEntity<String> buyCard(Long id, User user) {
        Card card = cardRepository.findById(id).orElse(null);
        if (card != null) {
            if (user.getMoney() > card.getPrice()) {
                user.addCard(card);
                user.setMoney(user.getMoney() - card.getPrice());
                return ResponseEntity.ok("Card bought successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not enough money to buy this card.");
            }
        }
        return ResponseEntity.ok("Card bought successfully.");
    }

    @GetMapping("/sell/{id}")
    public ResponseEntity<String> sellCard(Long id, User user) {
        Card card = cardRepository.findById(id).orElse(null);
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
