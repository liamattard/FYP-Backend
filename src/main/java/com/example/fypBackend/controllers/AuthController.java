package com.example.fypBackend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class AuthController {


    @Value("${instagram.client.id}")
    String clientId;

    @Value("${instagram.redirectUrl}")
    String redirectUri;

    @RequestMapping(value = "/auth/")
    public RedirectView authUser() {

        System.out.println("REDIRECT URI:"+redirectUri);

        String baseUrl = "https://api.instagram.com/oauth/authorize?";

        String instagramUrl = baseUrl + "client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&scope=user_profile,user_media&response_type=code";
        RedirectView redirectUrl = new RedirectView();
        redirectUrl.setUrl(instagramUrl);
        return redirectUrl;

    }

    @RequestMapping(value = "/getCode/", method = RequestMethod.GET)
    public @ResponseBody String getCode(@RequestParam("code") String code) {

        return code;
    }

}
