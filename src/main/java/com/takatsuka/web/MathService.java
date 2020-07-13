package com.takatsuka.web;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.Expression.ExpressionEval;
import com.takatsuka.web.math.rules.RuleLoader;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class MathService {
    private static final Logger logger = MathLogger.forCallingClass();

    private RuleLoader ruleLoader;
    private ExpressionEval evaluator;
    private HashMap<String, JSONObject> functions;

    MathService(RuleLoader ruleLoader) {
        this.evaluator = new ExpressionEval();
        functions = ruleLoader.loadRules();
    }

    public String evaluateExpression(String expression) {
        logger.trace("Evaluating expression '{}'", expression);
        return evaluator.Evaluate(expression);
    }
}
