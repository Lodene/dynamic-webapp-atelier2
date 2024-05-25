package com.sp.repo;

import com.sp.model.Market;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Iterable<Market> findByAction(String action);
    Optional<Market> findByCardId(Long cardId);

}
