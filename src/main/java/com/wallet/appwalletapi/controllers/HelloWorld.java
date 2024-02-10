package com.wallet.appwalletapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @GetMapping("/say")
    public final String sayHello(){
        return "Hello Elise";
    }
}
