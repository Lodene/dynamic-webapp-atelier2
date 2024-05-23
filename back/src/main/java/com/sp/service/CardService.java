package com.sp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.repo.CardRepository;
import com.sp.repo.UserRepository;

@Service
public class CardService {
    @Autowired
    CardRepository cRepository;
    @Autowired
    UserRepository userRepository;

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
    

    public List<Card> getCardsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return cRepository.findByUser(user);
    }

}