package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test() {
        throw new UnauthorizedException(
                "Invalid Email or Password");
    }
}