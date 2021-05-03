package com.example.fypBackend.repositories;

import com.example.fypBackend.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
