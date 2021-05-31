package com.example.fypBackend.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "access_token")
    private String InstaAccessToken;

    @Column(name = "fbaccess_token")
    private String fbAccessToken;

    @Column(name = "date_time")
    private java.sql.Timestamp date_time;

    @OneToOne
    @JoinColumn(name = "score_id")
    private Score score;

    @OneToOne
    @JoinColumn(name = "character_id")
    private Characteristics characteristics;

    public User() {

    }

    public User(String fbAccessToken) {

        this.date_time = java.sql.Timestamp.from(Instant.now());
        this.fbAccessToken = fbAccessToken;

    }

    public Integer getUser_id() {
        return this.user_id;
    }

    public Characteristics getCharacteristics_id() {
        return this.characteristics;

    }

    public void setCharacterId(Characteristics characteristics) {
        this.characteristics = characteristics;

    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getInstaAccessToken() {
        return this.InstaAccessToken;
    }

    public void setInstaAccessToken(String accessToken) {
        this.InstaAccessToken = accessToken;
    }

    public String getFbAccessToken() {
        return this.fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public java.sql.Timestamp getDate_time() {
        return this.date_time;
    }

    public void setDate_time(java.sql.Timestamp date_time) {
        this.date_time = date_time;
    }

    public Score getScore() {
        return this.score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

}
