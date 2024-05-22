package com.sp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

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
        user = userRepository.save(user); // Sauvegarder l'utilisateur avant de lui attribuer des cartes

        // Assigner les cartes initiales à l'utilisateur
        List<Card> allCards = cardRepository.findByUserIsNull();
        int totalCards = allCards.size();
        System.out.println("Total cards: " + totalCards);

        if (totalCards > 0) {
            Set<Card> assignedCards = new HashSet<>();
            Random random = new Random();

            while (assignedCards.size() < 5 && assignedCards.size() < totalCards) {
                int randomIndex = random.nextInt(totalCards);
                Card card = allCards.get(randomIndex);

                // S'assurer que la carte n'est pas déjà attribuée
                if (card.getUser() == null) {
                    assignedCards.add(card);
                    user.addCard(card);
                    card.setUser(user);
                    cardRepository.save(card);
                }
            }
            // Sauvegarder l'utilisateur avec les cartes attribuées
            user = userRepository.save(user);
        }

        return user;
    }

    public void addCardtoUser(Long id_card, Long id_user) {
        User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("User not found"));
        Card card = cardRepository.findById(id_card).orElseThrow(() -> new RuntimeException("Card not found"));
        if (card.getUser() == null) {
            user.addCard(card);
            card.setUser(user);
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("Card already owned by another user.");

    }

    public ResponseEntity<String> buyCard(Long id_card, Long id_user) {
        Card card = cardRepository.findById(id_card).orElseThrow(() -> new RuntimeException("Card not found"));
        if (card == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found.");
        }

        User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Card: " + card.getUser());
        if (card.getUser() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Card already owned by another user.");
        }

        if (card.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Card price not set.");
        }

        if (user.getMoney() > card.getPrice()) {
            user.addCard(card);
            card.setUser(user);
            user.setMoney(user.getMoney() - card.getPrice());
            userRepository.save(user);
            return ResponseEntity.ok("Card bought successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not enough money to buy this card.");
        }
    }
}