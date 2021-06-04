package com.example.fypBackend.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "score", schema = "public")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer score_id;

    @Column(name = "personalised_pref_rating")
    public Integer personalisedPrefRating;

    @Column(name = "personalised_over_rating")
    public Integer personalisedOverRating;

    @Column(name = "non_pref_rating")
    public Integer nonPrefRating;

    @Column(name = "non_over_rating")
    public Integer nonOverRating;

    public Score() {

    }
}
