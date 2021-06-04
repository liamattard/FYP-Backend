package com.example.fypBackend.repositories;

import com.example.fypBackend.entities.Score;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Integer> {

}
