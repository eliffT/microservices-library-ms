package com.turkcell.loan_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loans")
public class LoanController {

    @GetMapping
    public String getLoans(){
        System.out.println("get loans");
        return "Loan Controller";
    }
}
