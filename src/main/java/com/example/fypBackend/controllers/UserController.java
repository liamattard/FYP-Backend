
package com.example.fypBackend.controllers;

import java.util.List;

import com.example.fypBackend.entities.User;
import com.example.fypBackend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<User> getUsers() {
        List<User> users = userService.findAll();
        return users;

    }

}
