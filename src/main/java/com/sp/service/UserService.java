package com.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public User addUser(User user) {
        user.setMoney(500);
        userRepository.save(user);
        return user;
    }

    public Long login(String username, String password) {
        System.out.println("username: " + username);
        User user = userRepository.findByUsername(username).orElse(null);
        System.out.println(user);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return user.getId();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUserWithInitialCards(User user) {
        if (user.getMoney() != 500) {
            user.setMoney(500);
        }
        user = userRepository.save(user);

        // Assign initial cards to the user
        List<Card> allCards = cardRepository.findAll();
        int totalCards = allCards.size();
        System.out.println("Total cards: " + totalCards);
        if (totalCards > 5) {
            List<Card> assignedCards = new ArrayList<>();
            Random random = new Random();

            while (assignedCards.size() < 5) {
                int randomIndex = random.nextInt(totalCards);
                Card card = allCards.get(randomIndex);

                if (!assignedCards.contains(card) && !user.getCards().contains(card)) {
                    assignedCards.add(card);
                    user.addCard(card);
                }
            }
            // Save the user again with the assigned cards
            user = userRepository.save(user);
        }

        return user;
    }

    public void addCardtoUser(Long id_card, Long id_user) {
        User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("User not found"));
        Card card = cardRepository.findById(id_card).orElseThrow(() -> new RuntimeException("Card not found"));
        user.addCard(card);
        userRepository.save(user);
    }

    public ResponseEntity<String> buyCard(Long id_card, Long id_user) {
        Card card = cardRepository.findById(id_card).orElseThrow(() -> new RuntimeException("Card not found"));
        if (card == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found.");
        }

        User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getMoney() > card.getPrice()) {
            user.addCard(card);
            user.setMoney(user.getMoney() - card.getPrice());
            userRepository.save(user);
            return ResponseEntity.ok("Card bought successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not enough money to buy this card.");
        }
    }
}