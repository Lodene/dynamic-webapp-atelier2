package com.sp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


import com.sp.service.CardService;
import com.sp.service.UserService;
import com.sp.model.Card;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    CardService cardService;

    @Autowired
    UserService userService;

    @GetMapping("/add")
    public Card showAddForm() {
        return cardService.addCardDefault();
    }

    @GetMapping("/all")
	public Iterable<Card> getCards() {
		return cardService.getAll();
	}
}
