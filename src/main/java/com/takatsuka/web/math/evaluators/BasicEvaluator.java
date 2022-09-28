package com.takatsuka.web.math.evaluators;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.interpreter.Evaluator;
import com.takatsuka.web.math.utils.MathEvaluator;
import com.takatsuka.web.math.utils.MathMethod;

import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

@MathEvaluator
public class BasicEvaluator implements EvaluatorGrouping {

  private static final Logger logger = MathLogger.forCallingClass();
  private final MathContext mathContext;

  public BasicEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
  }

  @MathMethod("R")
  public String absoluteValue(BigDecimal input) {
    return String.valueOf(input.abs(mathContext));
  }

  @MathMethod("R")
  public String squareRoot(BigDecimal input) {
    return String.valueOf(input.sqrt(mathContext));
  }

  @MathMethod("R+")
  public String max(List<BigDecimal> input) {
    Optional<BigDecimal> result = input.stream().max(BigDecimal::compareTo);
    if (result.isPresent()) {
      return result.get().toString();
    } else {
      return Evaluator.DEFAULT;
    }
  }

  @MathMethod("R+")
  public String min(List<BigDecimal> input) {
    Optional<BigDecimal> result = input.stream().min(BigDecimal::compareTo);
    if (result.isPresent()) {
      return result.get().toString();
    } else {
      return Evaluator.DEFAULT;
    }
  }

  @MathMethod("Z+")
  public String gcd(List<BigInteger> input) {
    return gcdEuclidean(input).toString();
  }

  @MathMethod("Z+")
  public String lcm(List<BigInteger> input) {
    return lcmThroughGcd(input).toString();
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
}
