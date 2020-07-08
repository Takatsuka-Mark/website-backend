package com.takatsuka.web;

import com.takatsuka.web.Expression.ExpressionEval;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@CrossOrigin
public class MathController {

    private MathOps mathOps;
    private final String BASE_URL = "/math/";

    @PostConstruct
    public void init(){
        this.mathOps = new MathOps();
    }


    @GetMapping(BASE_URL + "prime")
    public String isPrime(@RequestParam(name="num", required = true) int num){
        return Integer.toString(num) + " " + (mathOps.isPrime(num)? "is prime.": "is not prime");
    }

    @GetMapping(BASE_URL + "primeFac")
    public String primeFactor(@RequestParam(name="num", required = true) int num){
        return mathOps.primeFactors(num).toString();
    }

    @GetMapping(BASE_URL + "evaluate")
    public String evaluate(@RequestParam(name="expression", required = true) String expression){
        ExpressionEval expressionEval = new ExpressionEval();
        return expressionEval.Evaluate(expression);
    }

    @GetMapping(BASE_URL + "totient")
    public String totient(@RequestParam(name="num", required = true) int num){
        return Integer.toString(mathOps.totient(mathOps.primeFactors(num)));
    }
}
