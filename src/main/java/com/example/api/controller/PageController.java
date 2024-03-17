package com.example.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage() {
        return "signup"; // main.html 파일의 경로에 따라 수정
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // main.html 파일의 경로에 따라 수정
    }

    @GetMapping("/coupon")
    public String coupon() {
        return "coupon";
    }
}
