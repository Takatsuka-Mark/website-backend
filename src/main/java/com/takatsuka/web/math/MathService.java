package com.takatsuka.web.math;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.interpreter.FunctionMapper;
import com.takatsuka.web.math.interpreter.MathParser;
import com.takatsuka.web.math.interpreter.FunctionLoader;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.MathContext;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MathService {
  private static final Logger logger = MathLogger.forCallingClass();
  private static final int EXEC_TIME_LIMIT = 2; // Time in seconds to allow execution
  private static final TimeLimiterConfig TIME_LIMITER_CONFIG = generateTimeLimiterConfig();

  private final MathParser mathParser;
  private final ExecutorService executorService;
  private final TimeLimiterRegistry timeLimiterRegistry;

  private int precision = 10;

  MathService(FunctionLoader functionLoader) {
    FunctionMapper functionMapper = new FunctionMapper(functionLoader.loadFunctions());
    this.mathParser = new MathParser(functionMapper);
    this.executorService = Executors.newCachedThreadPool();
    this.timeLimiterRegistry = TimeLimiterRegistry.of(TIME_LIMITER_CONFIG);
  }

  public String evaluateExpression(String expression) {
    logger.info("Evaluating expression '{}'", expression);
    String result = "";
    Stopwatch stopwatch = Stopwatch.createStarted();
    TimeLimiter timeLimiter = timeLimiterRegistry.timeLimiter("testTimeLimiter");

    try {
      result = timeLimiter.executeFutureSupplier(
          () -> CompletableFuture.supplyAsync(() -> mathParser.evaluate(expression)));
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

  private static TimeLimiterConfig generateTimeLimiterConfig() {
    return TimeLimiterConfig.custom()
        .cancelRunningFuture(/* cancelRunningFuture=*/ true)
        .timeoutDuration(Duration.ofSeconds(EXEC_TIME_LIMIT))
        .build();
  }
}
