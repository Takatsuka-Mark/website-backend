package com.takatsuka.web.math.evaluators;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.interpreter.Evaluator;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

@Component
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
