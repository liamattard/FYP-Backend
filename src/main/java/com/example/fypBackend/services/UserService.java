package com.example.fypBackend.services;

import java.util.List;
import java.util.Optional;

import com.example.fypBackend.entities.User;
import com.example.fypBackend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public int createUser(User user) {
        User newUser = userRepository.saveAndFlush(user);
        return newUser.getUser_id();

    }

    public void updateUser(User user) {
        userRepository.saveAndFlush(user);

    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

}
