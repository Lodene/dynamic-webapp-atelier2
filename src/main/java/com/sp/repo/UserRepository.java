package com.sp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sp.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
