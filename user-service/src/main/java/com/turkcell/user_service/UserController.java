package com.turkcell.user_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @GetMapping
    public String getUser(){
        System.out.println("get user");
        return "User Controller";
    }
}
