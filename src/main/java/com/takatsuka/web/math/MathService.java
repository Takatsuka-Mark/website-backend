package com.takatsuka.web.math;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.interpreter.FunctionMapper;
import com.takatsuka.web.math.interpreter.MathParser;
import com.takatsuka.web.math.interpreter.FunctionLoader;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MathService {
  private static final Logger logger = MathLogger.forCallingClass();

  private FunctionLoader functionLoader;
  private FunctionMapper functionMapper;
  private MathParser mathParser;

  MathService(FunctionLoader functionLoader) {
    this.functionLoader = functionLoader;
    this.functionMapper = new FunctionMapper(functionLoader.loadFunctions());
    this.mathParser = new MathParser(functionMapper);
  }

  public String evaluateExpression(String expression) {
    logger.trace("Evaluating expression '{}'", expression);
    return "Big O'l Eval time: " + mathParser.evaluate(expression);
  }
}
