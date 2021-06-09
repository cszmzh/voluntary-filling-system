package com.banana.volunteer.repository;

import com.banana.volunteer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    User findByUserName(String username);
}
