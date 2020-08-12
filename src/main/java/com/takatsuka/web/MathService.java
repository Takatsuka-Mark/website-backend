package com.takatsuka.web;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.Expression.ExpressionEval;
import com.takatsuka.web.math.rules.RuleLoader;
import com.takatsuka.web.rules.Function;
import com.takatsuka.web.rules.Operator;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MathService {
    private static final Logger logger = MathLogger.forCallingClass();

    private RuleLoader ruleLoader;
    private final ExpressionEval evaluator;
//    private HashMap<String, Rule> functions;


    MathService(RuleLoader ruleLoader) {
        HashMap<String, Function> functions = ruleLoader.loadFunctions();
        HashMap<String, Operator> operators = ruleLoader.loadOperators();
        this.evaluator = new ExpressionEval(functions, operators);
    }

    public String evaluateExpression(String expression) {
        logger.trace("Evaluating expression '{}'", expression);
        return evaluator.Evaluate(expression);
    }
}
