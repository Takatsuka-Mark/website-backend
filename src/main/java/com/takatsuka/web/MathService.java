package com.takatsuka.web;

import com.takatsuka.web.Expression.ExpressionEval;
import com.takatsuka.web.logging.MathLogger;
import org.slf4j.Logger;


public class MathService {
    private static final Logger logger = MathLogger.forCallingClass();

    private ExpressionEval evaluator;

    MathService() {
        this.evaluator = new ExpressionEval();
    }

    public String evaluateExpression(String expression) {
        logger.trace("Evaluating expression '{}'", expression);
        return evaluator.Evaluate(expression);
    }
}
