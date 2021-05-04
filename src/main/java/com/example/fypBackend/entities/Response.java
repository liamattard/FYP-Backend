package com.example.fypBackend.entities;

public class Response {
    private String access_token;
    private String user_id;

    public Response(String access_token, String user_id) {
        this.access_token = access_token;
        this.user_id = user_id;

    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
