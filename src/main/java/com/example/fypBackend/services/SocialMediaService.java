package com.example.fypBackend.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.example.fypBackend.entities.Characteristics;
import com.example.fypBackend.entities.Response;
import com.example.fypBackend.entities.User;
import com.example.fypBackend.entities.facebookLikes.AllLikes;
import com.example.fypBackend.entities.facebookLikes.LikesResponse;
import com.example.fypBackend.entities.facebookPhotos.Photo;
import com.example.fypBackend.entities.facebookPhotos.PhotosRoot;
import com.example.fypBackend.entities.instagramResponse.Datum;
import com.example.fypBackend.entities.instagramResponse.Image;
import com.example.fypBackend.entities.instagramResponse.Root;
import com.example.fypBackend.tools.Tools;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
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

    public User classifyPhotos(User user) throws Exception {

        Characteristics characteristics = new Characteristics();
        System.out.println("made it to here");
        System.out.println(characteristics);

        System.out.println("doing facebook");
        try {
            characteristics = classifyFacebookPhotos(user.getFbAccessToken(), characteristics);

        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("did facebook");
        System.out.println(characteristics);

        if (user.getInstaAccessToken() != null) {

            System.out.println("doing instagram");
            characteristics = classifyInstagramPhotos(user, characteristics);
            System.out.println("did instagram");
            System.out.println(characteristics);

        }
        user.setCharacterId(characteristics);
        user.setInstaAccessToken("");
        return user;
    }

    public Characteristics classifyInstagramPhotos(User user, Characteristics characteristics) throws Exception {

        String url = "https://graph.instagram.com/me/media?access_token=" + user.getInstaAccessToken();
        url = url + "&fields=id,caption";

        ResponseEntity<Root> responseTwo = this.restTemplate.getForEntity(url, Root.class);
        System.out.println("url");

        List<Datum> listOfImages = responseTwo.getBody().data;

        for (int i = 0; i < listOfImages.size(); i++) {

            System.out.println("meee");
            String urlTwo = "https://graph.instagram.com/" + listOfImages.get(i).id;
            urlTwo = urlTwo + "?fields=media_type,media_url";
            urlTwo = urlTwo + "&access_token=" + user.getInstaAccessToken();

            ResponseEntity<Image> responseThree = this.restTemplate.getForEntity(urlTwo, Image.class);
            String encoded_url = null;
            String media_url = responseThree.getBody().media_url;
            String media_type = responseThree.getBody().media_type;
            System.out.println(media_url);
            System.out.println("two");

            try {

                encoded_url = encodeValue(media_url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (media_type.equals("IMAGE")) {
                String urlFour = "http://localhost:8080/classify_image?url=" + encoded_url;
                ResponseEntity<String> responseFour = this.restTemplate.getForEntity(urlFour, String.class);
                int id = Integer.parseInt(responseFour.getBody());
                characteristics = Tools.changeUserCategory(id, characteristics);
                System.out.println("three");

            }

        }

        return characteristics;

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

    public String getInstaToken(String clientId, String appSecret, String redirectUri, String code) {

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

    public Characteristics classifyFacebookPhotos(String fbAccess_token, Characteristics characteristics)
            throws Exception {

        String url = "https://graph.facebook.com/v10.0/me?fields=id,name,photos{link}&access_token=";
        url = url + fbAccess_token;

        System.out.println(url);
        // ResponseEntity<PhotosRoot> responseTwo = this.restTemplate.getForEntity(url,
        // PhotosRoot.class);
        ResponseEntity<PhotosRoot> responseTwo = this.restTemplate.getForEntity(url, PhotosRoot.class, "{link}");

        System.out.println("aa wasal ta facebook");
        System.out.println("meet" + responseTwo.getBody().name);
        List<Photo> listOfImages = responseTwo.getBody().photos.data;

        for (int i = 0; i < listOfImages.size(); i++) {

            for (int j = 0; j < listOfImages.get(i).images.size(); j++) {
                String encoded_url = null;
                try {

                    System.out.println("aaaa..zz: " + listOfImages.get(i).images.get(j).source);
                    encoded_url = encodeValue(listOfImages.get(i).images.get(j).source);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String urlFour = "http://localhost:8080/classify_image?url=" + encoded_url;
                ResponseEntity<String> responseFour = this.restTemplate.getForEntity(urlFour, String.class);
                int id = Integer.parseInt(responseFour.getBody());
                characteristics = Tools.changeUserCategory(id, characteristics);

            }

        }

        return characteristics;
    }

    public User getUserLikes(User user) throws Exception {

        String nextPage = null;
        boolean pageOne = true;
        Characteristics characteristics = user.getCharacteristics_id();

        do {

            String url;

            if (nextPage == null) {

                url = "https://graph.facebook.com/v10.0/me?fields=likes{category}&access_token=";

                url = url + user.getFbAccessToken();
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

            if (likes.data.size() != 0) {

                for (int i = 0; i < likes.data.size(); i++) {

                    String cat = likes.data.get(i).category;
                    characteristics = Tools.updateUserLikes(cat, user.getCharacteristics_id());

                }
                if (likes.paging.next != null) {

                    nextPage = likes.paging.next;

                } else {
                    nextPage = null;
                }
            } else {
                nextPage = null;
            }

        } while (nextPage != null);

        user.setCharacterId(characteristics);
        user.setFbAccessToken("");
        return user;

    }

    public static String encodeValue(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

}
