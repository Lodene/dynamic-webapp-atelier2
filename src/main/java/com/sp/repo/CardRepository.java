package com.sp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.model.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
