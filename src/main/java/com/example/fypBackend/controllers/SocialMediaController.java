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
        /*
         * Redirects the user to the facebook oauth dialog screen. This should be the
         * first endpoint that is called from the frontend.
         */

        String baseUrl = "https://www.facebook.com/v10.0/dialog/oauth?";

        randomInt += 1;
        String facebookUrl = baseUrl + "client_id=" + fbClientId + "&redirect_uri=" + fbRedirectUri + "&state="
                + randomInt;
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(facebookUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/getFacebookToken", method = RequestMethod.GET)
    public RedirectView getFBToken(@RequestParam("code") String code) throws Exception {
        /*
         * If Facebook login was successful, the facebook oauth endpoint redirects the
         * user to this endpoint which retrieves the code and requests the user's access
         * token with it. A new user is created and their facebook access token is set.
         * Then the user will be redirected to page 2 of the frontend which will allow
         * them to connect their Instagram profile.
         *
         * @param code passed on from Facebook's oauth screen.
         */

        String fbAccessToken = socialMediaService.getFBToken(fbClientId, fbAppSecret, fbRedirectUri, code);

        User user = new User(fbAccessToken);
        int new_id = userService.createUser(user);

        String new_url = "https://www.touristplanner.xyz/screens/step2.html?id=" + new_id;

        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(new_url);
        return redirectUrl;

    }

    @RequestMapping(value = "/InstaAuth/")
    public RedirectView instaAuthUser(@RequestParam("id") int id) throws Exception {

        /*
         * Redirects the user to the Instagram oauth dialog screen.
         * 
         * @pram id an integer representing the user's id which will be sent with the
         * state param of the Instagram oauth url.
         */

        String baseUrl = "https://api.instagram.com/oauth/authorize?";

        String instagramUrl = baseUrl + "client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&scope=user_profile,user_media&response_type=code" + "&state=" + id;
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(instagramUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/getInstaToken/", method = RequestMethod.GET)
    public RedirectView getInstaToken(@RequestParam("code") String code, @RequestParam("state") int id) {
        /*
         * If Instagram login was successful, the Instagram oauth endpoint redirects the
         * user to this endpoint which retrieves the code and requests the user's access
         * token with it. Then the user will be redirected to the loading screen of the
         * frontend which will start the data collection proccess.
         *
         * @param code passed on from Facebook's oauth screen.
         * 
         * @param id user id of the user retrieved by the state param mentioned in the
         * /InstaAuth endpoint
         */

        String InstaAccessToken = socialMediaService.getInstaToken(clientId, appSecret, redirectUri, code);
        Optional<User> user = userService.findById(id);

        RedirectView redirectUrl = new RedirectView();

        if (!user.isPresent()) {

            redirectUrl.setUrl("https://www.touristplanner.xyz");
            return redirectUrl;
        }

        User current_user = user.get();

        String new_url = "https://www.touristplanner.xyz/screens/loading.html?id=" + current_user.getUser_id();
        current_user.setInstaAccessToken(InstaAccessToken);
        userService.updateUser(current_user);

        redirectUrl.setUrl(new_url);
        return redirectUrl;
    }

    @RequestMapping(value = "/classifyPhotos", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> classifyPhotos(@RequestParam("id") int id) throws Exception {
        /*
         * If login was successful, this endpoint gathers the images from the user's
         * connected social media. The photos are then sent to another local server
         * which classifies the images. NO IMAGES ARE STORED ON ANY SERVER (Code of
         * second local server is also public on github)!!
         * 
         * 
         * @param id representing the user
         */

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
        /*
         * After classifying the user's images, this endpoint classifies the user's
         * likes NO CLASSIFICATIONS ARE STORED ON ANY SERVER.
         * 
         * @param id representing the user
         */

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
