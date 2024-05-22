package com.sp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.Card;
import com.sp.repo.CardRepository;

@Service
public class CardService {
    @Autowired
    CardRepository cRepository;

    public Iterable<Card> getAll() {
        return cRepository.findAll();
    }

    public void createCard(Card card) {
        cRepository.save(card);
    }

    public Card getCard(Long id) {
        Optional<Card> card = cRepository.findById(id);
        return card.orElse(null);
    }

    public void updateCard(Card card) {
        cRepository.save(card);
    }

    public void deleteCard(Long id) {
        cRepository.deleteById(id);
    }

    public Card addCardDefault() {
        Card card = new Card();
        card.setName("Default Card");
        card.setPrice(100);
        card.setAttack(10);
        card.setDefense(10);
        cRepository.save(card);
        return card;
    }

    public Card findById(Long id) {
        return cRepository.findById(id).orElse(null);
    }

}