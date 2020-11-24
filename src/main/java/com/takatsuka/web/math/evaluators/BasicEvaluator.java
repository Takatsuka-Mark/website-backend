package com.takatsuka.web.math.evaluators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Optional;

public class BasicEvaluator {

  private final MathContext mathContext;
  private final String defaultVal;

  public BasicEvaluator(MathContext mathContext, String defaultVal) {
    this.mathContext = mathContext;
    this.defaultVal = defaultVal;
  }

  public String absoluteValue(BigDecimal input){
    return String.valueOf(input.abs(mathContext));
  }

  public String squareRoot(BigDecimal input){
    return String.valueOf(input.sqrt(mathContext));
  }

  public String max(BigDecimal ... input){
    Optional<BigDecimal> result = Arrays.stream(input).max(BigDecimal::compareTo);
    if (result.isPresent()) {
      return result.get().toString();
    } else {
      return defaultVal;
    }
  }

}
