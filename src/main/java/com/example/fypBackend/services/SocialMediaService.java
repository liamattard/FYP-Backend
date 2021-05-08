package com.example.fypBackend.services;

import com.example.fypBackend.entities.instagramResponse.Datum;
import com.example.fypBackend.entities.instagramResponse.Image;
import com.example.fypBackend.entities.instagramResponse.Root;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.example.fypBackend.entities.Response;
import com.example.fypBackend.entities.facebookLikes.AllLikes;
import com.example.fypBackend.entities.facebookLikes.Like;
import com.example.fypBackend.entities.facebookLikes.LikesResponse;

import org.springframework.util.LinkedMultiValueMap;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SocialMediaService {

    private final RestTemplate restTemplate;

    public SocialMediaService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String classifyPhotos(String access_token) throws Exception {

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
            String encoded_url = null;
            String media_url = responseThree.getBody().media_url;
            String media_type = responseThree.getBody().media_type;
            try {

                encoded_url = encodeValue(media_url);

            } catch (Exception e) {
                e.printStackTrace();
                return "Wrong";
            }
            if (media_type.equals("IMAGE")) {
                String urlFour = "http://localhost:5000/classify_image?url=" + encoded_url;
                ResponseEntity<String> responseFour = this.restTemplate.getForEntity(urlFour, String.class);
                allImages = allImages + "                     " + responseFour.getBody();

            }

        }

        return allImages;

    }

    public String getFBToken(String clientId, String appSecret, String redirectUri, String code) {

        String url = "https://graph.facebook.com/v10.0/oauth/access_token?";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("client_id", clientId);
        map.add("redirect_uri", redirectUri);
        map.add("client_secret", appSecret);
        map.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Response> response = this.restTemplate.postForEntity(url, request, Response.class);

        String access_token = response.getBody().getAccess_token();

        return access_token;
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

    public String getUserLikes(String access_token) throws Exception {

        String nextPage = null;
        String categories = "";
        boolean pageOne = true;

        do {

            String url;

            if (nextPage == null) {

                url = "https://graph.facebook.com/v10.0/me?fields=likes{category}&access_token=";

                url = url + access_token;
            } else {
                pageOne = false;

                url = nextPage;

            }

            System.out.println("URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> request = new HttpEntity<>(headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            UriComponents uriComponents = builder.build().encode();

            AllLikes likes = null;
            if (pageOne == true) {

                ResponseEntity<LikesResponse> response = this.restTemplate.exchange(uriComponents.toUri(),
                        HttpMethod.GET, request, LikesResponse.class);

                likes = response.getBody().likes;
            } else {

                ResponseEntity<AllLikes> response = this.restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET,
                        request, AllLikes.class);

                likes = response.getBody();

            }

            System.out.println("what??");
            for (int i = 0; i < likes.data.size(); i++) {

                categories += "  " + likes.data.get(i).category;
                System.out.println("CATEGORY: " + likes.data.get(i).category);

            }
            if (likes.paging.next != null) {

                nextPage = likes.paging.next;

            } else {
                nextPage = null;
            }

        } while (nextPage != null);

        return categories;

    }

    public String encodeValue(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

}
