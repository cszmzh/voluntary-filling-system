package com.banana.volunteer.service.Impl;

import com.banana.volunteer.entity.User;
import com.banana.volunteer.repository.UserRepository;
import com.banana.volunteer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findByUserId(Integer userId) {
        return repository.findById(userId).get();
    }

    @Override
    public User updateUser(User user) {
        return repository.saveAndFlush(user);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        repository.deleteById(userId);
    }
}
