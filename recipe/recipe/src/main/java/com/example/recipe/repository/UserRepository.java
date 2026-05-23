package com.example.recipe.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.recipe.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
