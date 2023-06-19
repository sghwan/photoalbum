package com.squarecross.photoalbum.ssr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homeRedirection() {
        return "redirect:/users/login";
    }
}
