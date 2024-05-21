package com.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public User addCardsInitial(User user) {
        List<Card> allCard = cardRepository.findAll();
        List<Long> allId = new ArrayList<>();
        int lastId = allCard.size();
        int i = 0;
        while (i < 5) {
            long random = (long) (Math.random() * lastId);
            if (!allId.contains(random)) {
                allId.add(random);
                Card card = cardRepository.findById(random).orElse(null);
                if (card != null) {
                    System.out.println(card.getName());
                    user.addCard(card);
                    i++;
                }
            }
        }
        userRepository.save(user);
        return user;
    }
}