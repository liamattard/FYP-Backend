package com.example.fypBackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "characteristics", schema = "public")
public class Characteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer characteristics_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "beach")
    private Integer beach;

    @Column(name = "museums")
    private Integer museums;

    public Integer getCharacteristics_id() {
        return this.characteristics_id;
    }

    public void setCharacteristics_id(Integer characteristics_id) {
        this.characteristics_id = characteristics_id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBeach() {
        return this.beach;
    }

    public void setBeach(Integer beach) {
        this.beach = beach;
    }

    public Integer getMuseums() {
        return this.museums;
    }

    public void setMuseums(Integer museums) {
        this.museums = museums;
    }

    public Integer getNight_club() {
        return this.night_club;
    }

    public void setNight_club(Integer night_club) {
        this.night_club = night_club;
    }

    public Integer getBars() {
        return this.bars;
    }

    public void setBars(Integer bars) {
        this.bars = bars;
    }

    public Integer getNature() {
        return this.nature;
    }

    public void setNature(Integer nature) {
        this.nature = nature;
    }
    public void setShopping(Integer shopping) {
        this.shopping = shopping;
    }

    @Column(name = "night_club")
    private Integer night_club;

    @Column(name = "bars")
    private Integer bars;

    @Column(name = "nature")
    private Integer nature;

    @Column(name = "shopping")
    private Integer shopping;

    public Integer getShopping() {
        return shopping;
    }

}
