package com.example.fypBackend.entities.facebookPhotos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photos {
    @JsonProperty("data")
    public List<Photo> data;
    public Paging paging;
}
