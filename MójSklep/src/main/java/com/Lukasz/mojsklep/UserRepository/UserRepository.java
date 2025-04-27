package com.lukasz.mojsklep.repository;

import com.lukasz.mojsklep.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
