package com.Lukasz.MojSklep.UserRepository;

import com.Lukasz.MojSklep.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}