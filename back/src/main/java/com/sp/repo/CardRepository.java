package com.sp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.model.Card;
import com.sp.model.User;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByUser(User user);
    }
