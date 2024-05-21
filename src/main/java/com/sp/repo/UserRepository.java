package com.sp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);
}
