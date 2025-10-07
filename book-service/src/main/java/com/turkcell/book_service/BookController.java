package com.turkcell.book_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @GetMapping
    public String getBooks(){
        System.out.println("getBooks");
        return "This is Book Service";
    }
}
