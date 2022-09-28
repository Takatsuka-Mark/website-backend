package com.takatsuka.web.math.evaluators;

import org.springframework.stereotype.Component;

import com.takatsuka.web.math.utils.MathEvaluator;
import com.takatsuka.web.math.utils.MathMethod;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

@MathEvaluator
public class RandomEvaluator implements EvaluatorGrouping {

  private final MathContext mathContext;
  private final Random randomSource;

  public RandomEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
    this.randomSource = new Random();
  }

  @MathMethod("ZZ")
  public String randomInt(BigInteger min, BigInteger max) {
    BigInteger randomNumber;
    BigInteger diff = max.subtract(min);

    // Generate random number 0 <= x < (max - min)
    do {
      randomNumber = new BigInteger(diff.bitLength(), randomSource);
    } while (randomNumber.compareTo(diff) >= 0);

    // Return the min + random number
    return min.add(randomNumber).toString();
  }

  public String randomFloat(BigDecimal min, BigDecimal max) {
    BigDecimal randomNumber;
    BigDecimal diff = max.subtract(min);

    // Generate a random number 0.0 <= x < (max - min)
    return "This needs completing!";
  }
}
