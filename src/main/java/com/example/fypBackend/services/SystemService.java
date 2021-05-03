package com.example.fypBackend.services;

import java.util.List;

import com.example.fypBackend.entities.System;
import com.example.fypBackend.repositories.SystemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemService {

    @Autowired
    private SystemRepository systemRepository;

    public List<System> findAll() {
        return systemRepository.findAll();
    }

}
