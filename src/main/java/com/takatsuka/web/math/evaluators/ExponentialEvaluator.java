package com.takatsuka.web.math.evaluators;

import java.math.BigDecimal;
import java.math.MathContext;

public class ExponentialEvaluator {

  private static final BigDecimal TWO = BigDecimal.ONE.add(BigDecimal.ONE);
  private final MathContext mathContext;
  private final String defaultVal;

  public ExponentialEvaluator(MathContext mathContext, String defaultVal) {
    this.mathContext = mathContext;
    this.defaultVal = defaultVal;
  }

  public String lg(BigDecimal input){
    return logn(TWO, input);
  }

  public String log(BigDecimal input) {
    return logn(BigDecimal.TEN, input);
  }

  public String logn(BigDecimal base, BigDecimal value) {
    return "Needs implementation.";
  }
}
