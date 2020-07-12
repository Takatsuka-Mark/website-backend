package com.takatsuka.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@CrossOrigin
public class MathController {

    private MathService mathService;
    private final String BASE_URL = "/math/";

    @PostConstruct
    public void init() {
        this.mathService = new MathService();
    }

    @GetMapping(BASE_URL + "evaluate")
    public String evaluate(@RequestParam(name = "expression", required = true) String expression) {
        return mathService.evaluateExpression(expression);
    }
}
