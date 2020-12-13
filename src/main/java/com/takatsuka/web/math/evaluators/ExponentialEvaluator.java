package com.takatsuka.web.math.evaluators;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class ExponentialEvaluator {

  private static final BigDecimal TWO = BigDecimal.ONE.add(BigDecimal.ONE);
  private final MathContext mathContext;

  public ExponentialEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
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

  public String modPow(BigInteger base, BigInteger exp, BigInteger mod) {
    return String.valueOf(base.modPow(exp, mod));
  }
}
