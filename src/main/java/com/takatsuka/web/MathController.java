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

    MathOps mathOps;

    @PostConstruct
    public void init(){
        this.mathOps = new MathOps();
    }



    @GetMapping("/math/prime")
    public String isPrime(@RequestParam(name="num", required = true) int num){
        return Integer.toString(num) + " " + (mathOps.isPrime(num)? "is prime.": "is not prime");
    }

    @GetMapping("/math/primeFac")
    public String primeFactor(@RequestParam(name="num", required = true) int num){
        return mathOps.primeFactors(num).toString();
    }

    @GetMapping("/math/totient")
    public String totient(@RequestParam(name="num", required = true) int num){
        return Integer.toString(mathOps.totient(mathOps.primeFactors(num)));
    }

    @GetMapping("/math/evaluate")
    public String evaluate(@RequestParam(name="expression", required = true) String expression){
        ExpressionEval expressionEval = new ExpressionEval();
        return expressionEval.Evaluate(expression);
    }
}
