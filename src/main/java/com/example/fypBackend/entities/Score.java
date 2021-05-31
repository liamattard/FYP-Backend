package com.example.fypBackend.entities;

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
    private Integer score_id;

    @Column(name = "system")
    private Integer system;

    @Column(name = "labelled_photos")
    private Integer labelledPhotos;

    @Column(name = "labelled_likes")
    private Integer labelledLikes;

    public Score() {
        this.labelledPhotos = 0;
        this.labelledLikes = 0;
    }

    public Integer getSystem() {
        return this.system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public Integer getlabelledPhotos() {
        return this.labelledPhotos;
    }

    public void incrementLabelledPhotos() {
        this.labelledPhotos += 1;
    }

    public void incrementLabelledLikes() {
        this.labelledLikes += 1;
    }
}
