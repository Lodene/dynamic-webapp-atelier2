package com.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sp.model.Card;
import com.sp.model.User;
import com.sp.repo.CardRepository;
import com.sp.repo.UserRepository;

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
        Optional<User> optUser = userRepository.findByUsername(username);
        if (!optUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = optUser.get();
        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return user.getId();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUserWithInitialCards(User user) {

        user = userRepository.save(user);

        // Assign initial cards to the user
        List<Card> allCards = cardRepository.findAll();
        int totalCards = allCards.size();

        if (totalCards < 5) {
            throw new IllegalArgumentException("Not enough cards available to assign to user");
        }

        List<Card> assignedCards = new ArrayList<>();
        Random random = new Random();

        while (assignedCards.size() < 5) {
            int randomIndex = random.nextInt(totalCards);
            Card card = allCards.get(randomIndex);

            if (!assignedCards.contains(card)) {
                assignedCards.add(card);
                user.addCard(card);
            }
        }
        // Save the user again with the assigned cards
        user = userRepository.save(user);
        return user;
    }

    public void addCardtoUser(Long id_card, Long id_user) {
        User user = userRepository.findById(id_user).orElseThrow(() -> new RuntimeException("User not found"));
        Card card = cardRepository.findById(id_card).orElseThrow(() -> new RuntimeException("Card not found"));
        user.addCard(card);
        userRepository.save(user);
    }
}