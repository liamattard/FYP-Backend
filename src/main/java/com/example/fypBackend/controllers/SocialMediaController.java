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

//@CrossOrigin(origins = "https://www.touristplanner.xyz")
@CrossOrigin(origins = "*")
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

    public static int randomInt = 0;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/fbAuth/")
    public RedirectView fbAuthUser() {

        String baseUrl = "https://www.facebook.com/v10.0/dialog/oauth?";

        randomInt += 1;
        String facebookUrl = baseUrl + "client_id=" + fbClientId + "&redirect_uri=" + fbRedirectUri + "&state="
                + randomInt;
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(facebookUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/InstaAuth/")
    public RedirectView instaAuthUser(@RequestParam("id") int id) throws Exception {

        String baseUrl = "https://api.instagram.com/oauth/authorize?";

        String instagramUrl = baseUrl + "client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&scope=user_profile,user_media&response_type=code" + "&state=" + id;
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(instagramUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/getFacebookToken", method = RequestMethod.GET)
    public RedirectView getFBToken(@RequestParam("code") String code) throws Exception {

        String fbAccessToken = socialMediaService.getFBToken(fbClientId, fbAppSecret, fbRedirectUri, code);

        User user = new User(fbAccessToken);
        int new_id = userService.createUser(user);

        String new_url = "https://www.touristplanner.xyz/screens/step2.html?id=" + new_id;

        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(new_url);
        return redirectUrl;

    }

    @RequestMapping(value = "/getInstaToken/", method = RequestMethod.GET)
    public RedirectView getInstaToken(@RequestParam("code") String code, @RequestParam("state") int id) {

        String InstaAccessToken = socialMediaService.getInstaToken(clientId, appSecret, redirectUri, code);
        Optional<User> user = userService.findById(id);

        RedirectView redirectUrl = new RedirectView();

        if (!user.isPresent()) {

            redirectUrl.setUrl("https://www.touristplanner.xyz");
            return redirectUrl;
        }

        User current_user = user.get();

        String new_url = "https://www.touristplanner.xyz/screens/loading.html?id=" + current_user.getUser_id()
                + "&withInsta=1";
        current_user.setInstaAccessToken(InstaAccessToken);
        userService.updateUser(current_user);

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

    @RequestMapping(value = "/getUserLikes", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserLikes(@RequestParam("id") int id) throws Exception {

        Optional<User> user = userService.findById(id);

        if (!user.isPresent()) {

            return ResponseEntity.badRequest().body("User Not Found");

        }

        User final_user = socialMediaService.getUserLikes(user.get());
        Characteristics characteristics = characteristicsRespository.saveAndFlush(final_user.getCharacteristics_id());
        final_user.setCharacterId(characteristics);
        final_user = userRepository.saveAndFlush(final_user);

        return ResponseEntity.ok("User preferences gathered");
    }
}
