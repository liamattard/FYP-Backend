package com.example.fypBackend.entities.instagramResponse;

import java.util.List;

import com.example.fypBackend.entities.instagramResponse.Datum;
import com.example.fypBackend.entities.instagramResponse.Paging;

public class Root {
    public List<Datum> data;
    public Paging paging;
}
