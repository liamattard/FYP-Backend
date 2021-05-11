package com.example.fypBackend.controllers;

import java.util.Optional;

import com.example.fypBackend.entities.Characteristics;
import com.example.fypBackend.entities.User;
import com.example.fypBackend.repositories.UserRepository;
import com.example.fypBackend.repositories.CharacteristicsRepository;
import com.example.fypBackend.services.SocialMediaService;
import com.example.fypBackend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin(origins = "https://www.touristplanner.xyz")
@RestController
public class SocialMediaController {

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CharacteristicsRepository characteristicsRespository;

    @Value("${instagram.client.id}")
    String clientId;

    @Value("${instagram.redirectUrl}")
    String redirectUri;

    @Value("${instagram.appSecret}")
    String appSecret;

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

        String facebookUrl = baseUrl + "client_id=" + fbClientId + "&redirect_uri=" + fbRedirectUri + "&state=" + id;
        System.out.println("URL= " + facebookUrl);
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(facebookUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/getFacebookToken", method = RequestMethod.GET)
    public RedirectView getFBToken(@RequestParam("code") String code, @RequestParam("state") int id) throws Exception {

        String fbaccessToken = socialMediaService.getFBToken(fbClientId, fbAppSecret, fbRedirectUri, code);
        Optional<User> user = userService.findById(id);

        RedirectView redirectUrl = new RedirectView();
        if (!user.isPresent()) {

            redirectUrl.setUrl("https://www.touristplanner.xyz");
        }

        User current_user = user.get();
        current_user.setFbAccessToken(fbaccessToken);
        userService.updateUser(current_user);
        redirectUrl.setUrl("https://www.touristplanner.xyz/screens/loading.html?id=" + id);

        return redirectUrl;
    }

    @RequestMapping(value = "/getToken/", method = RequestMethod.GET)
    public RedirectView getToken(@RequestParam("code") String code) {

        String accessToken = socialMediaService.getToken(clientId, appSecret, redirectUri, code);
        User user = new User(accessToken);
        int new_id = userService.createUser(user);

        String new_url = "https://www.touristplanner.xyz/screens/step2.html?id=" + new_id;
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(new_url);
        return redirectUrl;
    }

    @RequestMapping(value = "/classifyPhotos", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> classifyPhotos(@RequestParam("id") int id) throws Exception {

        Optional<User> user = userService.findById(id);
        User final_user = null;

        if (user.isPresent()) {

            try {

                final_user = socialMediaService.classifyPhotos(user.get());
                Characteristics characteristics = characteristicsRespository
                        .saveAndFlush(final_user.getCharacteristics_id());
                final_user.setCharacterId(characteristics);
                final_user = userRepository.saveAndFlush(final_user);

            } catch (Exception e) {

                return ResponseEntity.badRequest().body("User preferences not gathered");

            }

        } else {

            return ResponseEntity.badRequest().body("User preferences not gathered");

        }
        return ResponseEntity.ok("User preferences gathered");

    }

    @RequestMapping(value = "/getUserLikes", method = RequestMethod.GET)
    public @ResponseBody String getUserLikes(@RequestParam("id") int id) throws Exception {

        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {

            return "WRONG";

        }

        String test = socialMediaService.getUserLikes(user.get().getFbAccessToken());

        return test;
    }
}
