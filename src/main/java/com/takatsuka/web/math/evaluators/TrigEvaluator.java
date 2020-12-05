package com.takatsuka.web.math.evaluators;

import java.math.MathContext;

public class TrigEvaluator {

  private final MathContext mathContext;
  private final String defaultVal;

  public TrigEvaluator(MathContext mathContext, String defaultVal) {
    this.mathContext = mathContext;
    this.defaultVal = defaultVal;
  }

  /** Standard */
  public String sin(Double input) {
    return String.valueOf(Math.sin(input));
  }

  public String cos(Double input) {
    return String.valueOf(Math.cos(input));
  }

  public String tan(Double input) {
    return String.valueOf(Math.tan(input));
  }

  /** Inverse */
  public String asin(Double input) {
    return String.valueOf(Math.asin(input));
  }

  public String acos(Double input) {
    return String.valueOf(Math.acos(input));
  }

  public String atan(Double input) {
    return String.valueOf(Math.atan(input));
  }

  /** Hyperbolic */
  public String sinh(Double input) {
    return String.valueOf(Math.sinh(input));
  }

  public String cosh(Double input) {
    return String.valueOf(Math.cosh(input));
  }

  public String tanh(Double input) {
    return String.valueOf(Math.tanh(input));
  }

  /** Reciprocal */
  public String sec(Double input) {
    return String.valueOf(1 / Double.parseDouble(cos(input)));
  }

  public String csc(Double input) {
    return String.valueOf(1 / Double.parseDouble(sin(input)));
  }

  public String cot(Double input) {
    return String.valueOf(1 / Double.parseDouble(tan(input)));
  }

  /** Inverse Reciprocal */
  // TODO(mark): Verify that these are as expected
  public String asec(Double input) {
    return String.valueOf(1 / Double.parseDouble(acos(input)));
  }

  public String acsc(Double input) {
    return String.valueOf(1 / Double.parseDouble(asin(input)));
  }

  public String acot(Double input) {
    return String.valueOf(1 / Double.parseDouble(atan(input)));
  }

  /** Hyperbolic Reciprocal */
  // TODO(mark): Verify that these are as expected
  public String sech(Double input) {
    return String.valueOf(1 / Double.parseDouble(cosh(input)));
  }

  public String csch(Double input) {
    return String.valueOf(1 / Double.parseDouble(sinh(input)));
  }

  public String coth(Double input) {
    return String.valueOf(1 / Double.parseDouble(tanh(input)));
  }
}
