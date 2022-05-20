package com.takatsuka.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HomeController {
    @Autowired
    BuildProperties buildProperties;

    @GetMapping("/isUp")
    public String isUp() {
        return "Yes!";
    }

    @GetMapping("/version")
    public String version() {
        return buildProperties.getVersion();
    }
}
