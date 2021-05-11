package com.example.fypBackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "characteristics", schema = "public")
public class Characteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer character_id;

    @Column(name = "beach")
    private Integer beach;

    @Column(name = "museums")
    private Integer museums;

    @Column(name = "night_club")
    private Integer night_club;

    @Column(name = "bars")
    private Integer bars;

    @Column(name = "nature")
    private Integer nature;

    @Column(name = "shopping")
    private Integer shopping;

    public Characteristics() {
        this.bars = 0;
        this.beach = 0;
        this.nature = 0;
        this.shopping = 0;
        this.museums = 0;
        this.night_club = 0;
    }

    public Integer getCharacteristics_id() {
        return this.character_id;
    }

    public Integer getBeach() {
        return this.beach;
    }

    public void setBeach() {
        this.beach += 1;
    }

    public Integer getMuseums() {
        return this.museums;
    }

    public void setMuseums() {
        this.museums += 1;
    }

    public Integer getNight_club() {
        return this.night_club;
    }

    public void setNight_club() {
        this.night_club += 1;
    }

    public Integer getBars() {
        return this.bars;
    }

    public void setBars() {
        this.bars += 1;
    }

    public Integer getNature() {
        return this.nature;
    }

    public void setNature() {
        this.nature += 1;
    }

    public void setShopping() {
        this.shopping += 1;
    }

    public Integer getShopping() {
        return shopping;
    }

}
