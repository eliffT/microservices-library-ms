package com.turkcell.fine_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/fines")
public class FineController {

    @GetMapping
    public String getFines(){
        System.out.println("getFines");
        return "Fine Controller";
    }
}
