package com.example.fypBackend.controllers;

import java.util.Optional;

import com.example.fypBackend.entities.User;
import com.example.fypBackend.services.SocialMediaService;
import com.example.fypBackend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@CrossOrigin(origins = "https://www.touristplanner.xyz")
public class SocialMediaController {

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private UserService userService;

    @Value("${instagram.client.id}")
    String clientId;

    @Value("${instagram.redirectUrl}")
    String redirectUri;

    @Value("${instagram.appSecret}")
    String appSecret;

    @Value("${facebook.state}")
    String fbState;

    @Value("${facebook.client.id}")
    String fbClientId;

    @Value("${facebook.redirectUrl}")
    String fbRedirectUri;

    @Value("${facebook.appSecret}")
    String fbAppSecret;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/auth/")
    public RedirectView fbauthUser() {

        System.out.println("REDIRECT URI:" + redirectUri);

        String baseUrl = "https://api.instagram.com/oauth/authorize?";

        String instagramUrl = baseUrl + "client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&scope=user_profile,user_media&response_type=code";
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(instagramUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/fbAuth/")
    public RedirectView fbAuthUser(@RequestParam("id") int id) throws Exception {

        System.out.println("REDIRECT URI:" + redirectUri);

        String baseUrl = "https://www.facebook.com/v10.0/dialog/oauth?";

        String updatedURL = fbRedirectUri + "?id=" + id;
        updatedURL = socialMediaService.encodeValue(updatedURL);
        String facebookUrl = baseUrl + "client_id=" + fbClientId + "&redirect_uri=" + updatedURL + "&state=" + fbState;
        System.out.println("URL= " + facebookUrl);
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(facebookUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/getFacebookToken", method = RequestMethod.GET)
    public @ResponseBody String getFBToken(@RequestParam("code") String code, @RequestParam("id") int id)
            throws Exception {

        String updatedURL = fbRedirectUri + "?id=" + id;
        updatedURL = socialMediaService.encodeValue(updatedURL);
        String accessToken = socialMediaService.getFBToken(fbClientId, fbAppSecret, updatedURL, code);
        System.out.println("GOT ACCESS TOKEN!!: " + accessToken);

        return "Updated user" + id;
    }

    @RequestMapping(value = "/getToken/", method = RequestMethod.GET)
    public @ResponseBody int getToken(@RequestParam("code") String code) {

        String accessToken = socialMediaService.getToken(clientId, appSecret, redirectUri, code);
        User user = new User(accessToken);
        int new_id = userService.createUser(user);

        return new_id;
    }

    @RequestMapping(value = "/classifyPhotos", method = RequestMethod.GET)
    public @ResponseBody String classifyPhotos(@RequestParam("id") int id) throws Exception {

        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {

            return "Not Found";

        }

        String categories;

        try {
            categories = socialMediaService.classifyPhotos(user.get().getAccessToken());

        } catch (Exception e) {

            categories = "WWRONG";
        }

        return categories;
    }

}
