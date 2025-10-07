package com.turkcell.author_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

   @GetMapping
    public String getAuthor(){
       System.out.println("Get Author Controller");
       return "Author Controller";
    }


}
