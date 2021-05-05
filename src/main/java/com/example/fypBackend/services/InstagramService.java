package com.example.fypBackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.fypBackend.entities.instagramResponse.Datum;
import com.example.fypBackend.entities.instagramResponse.Image;
import com.example.fypBackend.entities.instagramResponse.Root;

import java.util.List;

import com.example.fypBackend.entities.Response;

import org.springframework.util.LinkedMultiValueMap;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class InstagramService {

    private final RestTemplate restTemplate;

    public InstagramService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String classifyPhotos(String access_token) {

        String urlTwo = "https://graph.instagram.com/me/media?access_token=" + access_token;
        urlTwo = urlTwo + "&fields=id,caption";

        ResponseEntity<Root> responseTwo = this.restTemplate.getForEntity(urlTwo, Root.class);

        List<Datum> listOfImages = responseTwo.getBody().data;

        String allImages = " ";
        for (int i = 0; i < listOfImages.size(); i++) {
            String urlThree = "https://graph.instagram.com/" + listOfImages.get(i).id;
            urlThree = urlThree + "?fields=id,media_type,media_url,username,timestamp";
            urlThree = urlThree + "&access_token=" + access_token;

            ResponseEntity<Image> responseThree = this.restTemplate.getForEntity(urlThree, Image.class);
            allImages = allImages + "\n" + responseThree.getBody().media_url;

        }

        return allImages;

    }

    public String getToken(String clientId, String appSecret, String redirectUri, String code) {

        String url = "https://api.instagram.com/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("client_id", clientId);
        map.add("client_secret", appSecret);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectUri);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Response> response = this.restTemplate.postForEntity(url, request, Response.class);

        String access_token = response.getBody().getAccess_token();

        return access_token;
    }

}
