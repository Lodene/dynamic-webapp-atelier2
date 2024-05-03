package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sp.repo.CardRepository;
import com.sp.model.Card;

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

        model.addAttribute("card", card);
        return "addCardForm";
    }

    @PostMapping("/add")
    public String addCard(Card card, Model model) {
        cardRepository.save(card);
        return "redirect:/";
    }
}
