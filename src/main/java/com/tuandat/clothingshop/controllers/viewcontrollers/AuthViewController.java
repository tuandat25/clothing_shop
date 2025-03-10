package com.tuandat.clothingshop.controllers.viewcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthViewController {
    @GetMapping
    public String index() {
        return "auth/auth";
    }
}
