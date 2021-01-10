package com.takatsuka.web.math;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.interpreter.FunctionMapper;
import com.takatsuka.web.math.interpreter.MathParser;
import com.takatsuka.web.math.interpreter.FunctionLoader;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.MathContext;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MathService {
  private static final Logger logger = MathLogger.forCallingClass();
  private static final int EXEC_TIME_LIMIT = 2; // Time in seconds to allow execution

  private final MathParser mathParser;
  private final SimpleTimeLimiter simpleTimeLimiter;

  private int precision = 10;

  MathService(FunctionLoader functionLoader) {
    FunctionMapper functionMapper = new FunctionMapper(functionLoader.loadFunctions());
    this.mathParser = new MathParser(functionMapper);
    ExecutorService executorService = Executors.newCachedThreadPool();
    this.simpleTimeLimiter = SimpleTimeLimiter.create(executorService);
  }

  public String evaluateExpression(String expression) {
    logger.info("Evaluating expression '{}'", expression);
    String result = "";
    Stopwatch stopwatch = Stopwatch.createStarted();
    try {
      result = simpleTimeLimiter.callWithTimeout(
          () -> new DoEval(expression).call(), EXEC_TIME_LIMIT, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      logger.error("The Executor timed out.");
      result = String.format("The execution time limit (%s seconds) was reached.", EXEC_TIME_LIMIT);
    } catch (Exception e) {
      logger.error("An error was caught by the catch-all: ", e);
      result =
          "There was an error evaluating your expression. "
              + "Check your syntax! A live syntax checker is coming soon.";
    }
    logger.info(
        "Expression '{}' evaluated to '{}' in '{}' millis",
        expression,
        result,
        stopwatch.elapsed().toMillis());
    return result;
  }

  private class DoEval implements Callable<String> {
    private final String expression;

    public DoEval(String expression) {
      this.expression = expression;
    }

    public String call() {
      return mathParser.evaluate(expression);
    }
  }

  public void setPrecision(int newPrecision) {
    precision = newPrecision;
  }

  public int getPrecision() {
    return precision;
  }

  @Bean
  public MathContext generateMathContext() {
    return new MathContext(precision);
  }
}
