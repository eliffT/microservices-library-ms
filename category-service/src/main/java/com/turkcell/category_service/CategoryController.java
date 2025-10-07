package com.turkcell.category_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @GetMapping
    public String getCategories(){
        System.out.println("Get Categories");
        return "Categories Controller";
    }
}
