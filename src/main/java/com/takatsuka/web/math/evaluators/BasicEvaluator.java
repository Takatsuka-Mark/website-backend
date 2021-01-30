package com.takatsuka.web.math.evaluators;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.interpreter.Evaluator;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class BasicEvaluator {

  private static final Logger logger = MathLogger.forCallingClass();
  private final MathContext mathContext;

  public BasicEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
  }

  public String absoluteValue(BigDecimal input) {
    return String.valueOf(input.abs(mathContext));
  }

  public String squareRoot(BigDecimal input) {
    return String.valueOf(input.sqrt(mathContext));
  }

  public String max(List<BigDecimal> input) {
    Optional<BigDecimal> result = input.stream().max(BigDecimal::compareTo);
    if (result.isPresent()) {
      return result.get().toString();
    } else {
      return Evaluator.DEFAULT;
    }
  }

  public String min(List<BigDecimal> input) {
    Optional<BigDecimal> result = input.stream().min(BigDecimal::compareTo);
    if (result.isPresent()) {
      return result.get().toString();
    } else {
      return Evaluator.DEFAULT;
    }
  }

  public String gcd(List<BigInteger> input) {
    return gcdEuclidean(input).toString();
  }

  public String lcm(List<BigInteger> input) {
    return lcmThroughGcd(input).toString();
  }

  public String pow(BigInteger base, BigInteger exp) {
    boolean isNegative = false;
    // DO MATH
    if (exp.signum() < 0) {
      isNegative = true;
      exp = exp.abs();
    }

    BigInteger result = BigInteger.ONE;

    while (exp.signum() > 0) {
      throwIfInterrupted(); // Catch for interrupted thread.

      if (exp.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
        result = result.multiply(base);
      }
      base = base.multiply(base);
      exp = exp.shiftLeft(1);
    }

    return result.toString();
  }

  private BigInteger gcdEuclidean(List<BigInteger> input) {
    BigInteger result = input.get(0);
    for (int i = 1; i < input.size(); i++) {
      result = gcdEuclidean(input.get(i), result);
    }

    return result;
  }

  private BigInteger gcdEuclidean(BigInteger A, BigInteger B) {
    if (A.equals(BigInteger.ZERO)) {
      return B;
    } else if (B.equals(BigInteger.ZERO)) {
      return A;
    }

    return gcdEuclidean(B, A.mod(B));
  }

  private BigInteger lcmThroughGcd(List<BigInteger> input) {
    BigInteger result = input.get(0);
    for (int i = 1; i < input.size(); i++) {
      result = (input.get(i).multiply(result)).divide(gcdEuclidean(input.get(i), result));
    }

    return result;
  }

  private void throwIfInterrupted() {
    if (Thread.currentThread().isInterrupted()) {
      logger.error("Timeout in BasicEvaluator.");
      throw new RuntimeException("Timeout in BasicEvaluator", new TimeoutException());
    }
  }
}
