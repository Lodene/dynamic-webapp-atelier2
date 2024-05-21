package com.sp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
