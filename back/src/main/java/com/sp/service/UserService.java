package com.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.repo.CardRepository;
import com.sp.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUserWithInitialCards(User user) {
        if (user.getMoney() != 500) {
            user.setMoney(500);
        }
        user = userRepository.save(user);

        List<Card> allCards = cardRepository.findAll();
        int totalCards = allCards.size();
        if (totalCards > 5) {
            List<Card> assignedCards = new ArrayList<>();
            Random random = new Random();

            while (assignedCards.size() < 5) {
                int randomIndex = random.nextInt(totalCards);
                Card card = allCards.get(randomIndex);
                if (!assignedCards.contains(card) && card.getUser() == null){
                    assignedCards.add(card);
                    card.setUser(user);
                    cardRepository.save(card);
                }
            }
            System.out.println("Assigned cards: " + assignedCards);
        }

        return user;
    }

}