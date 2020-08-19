package com.takatsuka.web.math;

import com.takatsuka.web.MathService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MathController {

    private MathService mathService;
    private final String BASE_URL = "/math/";

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping(BASE_URL + "evaluate")
    public String evaluate(@RequestParam(name = "expression") String expression) {
        return mathService.evaluateExpression(expression);
    }
}
