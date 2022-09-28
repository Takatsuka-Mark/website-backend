package com.takatsuka.web;

import java.util.Properties;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HomeController {
    // @Autowired // https://stackoverflow.com/a/68686686
    BuildProperties buildProperties = new BuildProperties(new Properties());

    @GetMapping("/isUp")
    public String isUp() {
        return "Yes!";
    }

    @GetMapping("/version")
    public String version() {
        return buildProperties.getVersion();
    }
}
