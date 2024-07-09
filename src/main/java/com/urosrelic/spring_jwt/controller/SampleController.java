package com.urosrelic.spring_jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/private")
    private String privateMessage() {
        return "PRIVATE MESASGE";
    }
}
