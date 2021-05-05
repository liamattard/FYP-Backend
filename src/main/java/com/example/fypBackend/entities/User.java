package com.example.fypBackend.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "date_time")
    private java.sql.Timestamp date_time;

    @Column(name = "score")
    private int score;

    @ManyToOne
    @JoinColumn(name = "system_id")
    private System system;

    public User() {

    }

    public User(String accessToken) {

        this.date_time = java.sql.Timestamp.from(Instant.now());
        this.accessToken = accessToken;
        this.score = 0;
        this.system = null;

    }

    public Integer getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public java.sql.Timestamp getDate_time() {
        return this.date_time;
    }

    public void setDate_time(java.sql.Timestamp date_time) {
        this.date_time = date_time;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public System getSystem() {
        return this.system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

}
