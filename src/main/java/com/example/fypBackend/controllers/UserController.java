
package com.example.fypBackend.controllers;

import java.util.List;

import com.example.fypBackend.entities.User;
import com.example.fypBackend.entities.System;
import com.example.fypBackend.services.UserService;
import com.example.fypBackend.services.SystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SystemService systemService;

    @RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<User> getUsers() {
        List<User> users = userService.findAll();
        return users;

    }

    @GetMapping("/systems")
    public List<System> getSystems() {
        List<System> systems = systemService.findAll();
        return systems;

    }

}
