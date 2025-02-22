package com.wrap.it.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    @GetMapping("/login")
    public String login() {
        return "Hello world!";
    }
}
