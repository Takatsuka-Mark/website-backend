package com.takatsuka.web;

import com.takatsuka.web.Expression.ExpressionEval;


public class MathService {
    private ExpressionEval evaluator;

    MathService(){
        this.evaluator = new ExpressionEval();
    }


    public String evaluateExpression(String expression){
        return evaluator.Evaluate(expression);
    }
}
