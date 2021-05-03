package com.example.fypBackend.services;

import java.util.List;

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

}
