package com.example.fypBackend.entities;

public class TokenRequest {

    private String grantType;
    private String code;
    private String clientId;
    private String redirectUri;
    private String appSecret;

    public TokenRequest(String clientId, String redirectUri, String code, String appSecret) {

        this.grantType = "authorization_code";
        this.code = code;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.appSecret = appSecret;

    }

}
