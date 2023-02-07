package com.takatsuka.web.math.evaluators;

import org.springframework.stereotype.Component;

import com.takatsuka.web.math.utils.MathEvaluator;
import com.takatsuka.web.math.utils.MathMethod;

import java.math.MathContext;

@MathEvaluator
public class TrigEvaluator implements EvaluatorGrouping {

  private final MathContext mathContext;

  public TrigEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
  }

  /** Standard */
  @MathMethod("r")
  public String sin(Double input) {
    return String.valueOf(Math.sin(input));
  }

  @MathMethod("r")
  public String cos(Double input) {
    return String.valueOf(Math.cos(input));
  }

  @MathMethod("r")
  public String tan(Double input) {
    return String.valueOf(Math.tan(input));
  }

  /** Inverse */
  @MathMethod("r")
  public String asin(Double input) {
    return String.valueOf(Math.asin(input));
  }

  @MathMethod("r")
  public String acos(Double input) {
    return String.valueOf(Math.acos(input));
  }

  @MathMethod("r")
  public String atan(Double input) {
    return String.valueOf(Math.atan(input));
  }

  /** Hyperbolic */
  @MathMethod("r")
  public String sinh(Double input) {
    return String.valueOf(Math.sinh(input));
  }

  @MathMethod("r")
  public String cosh(Double input) {
    return String.valueOf(Math.cosh(input));
  }

  @MathMethod("r")
  public String tanh(Double input) {
    return String.valueOf(Math.tanh(input));
  }

  /** Reciprocal */
  @MathMethod("r")
  public String sec(Double input) {
    return String.valueOf(1 / Double.parseDouble(cos(input)));
  }

  @MathMethod("r")
  public String csc(Double input) {
    return String.valueOf(1 / Double.parseDouble(sin(input)));
  }

  @MathMethod("r")
  public String cot(Double input) {
    return String.valueOf(1 / Double.parseDouble(tan(input)));
  }

  /** Inverse Reciprocal */
  // TODO(mark): Verify that these are as expected
  @MathMethod("r")
  public String asec(Double input) {
    return String.valueOf(1 / Double.parseDouble(acos(input)));
  }

  @MathMethod("r")
  public String acsc(Double input) {
    return String.valueOf(1 / Double.parseDouble(asin(input)));
  }

  @MathMethod("r")
  public String acot(Double input) {
    return String.valueOf(1 / Double.parseDouble(atan(input)));
  }

  /** Hyperbolic Reciprocal */
  // TODO(mark): Verify that these are as expected
  @MathMethod("r")
  public String sech(Double input) {
    return String.valueOf(1 / Double.parseDouble(cosh(input)));
  }

  @MathMethod("r")
  public String csch(Double input) {
    return String.valueOf(1 / Double.parseDouble(sinh(input)));
  }

  @MathMethod("r")
  public String coth(Double input) {
    return String.valueOf(1 / Double.parseDouble(tanh(input)));
  }
}
