package com.takatsuka.web.math.evaluators;

import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class BasicEvaluatorTest {

  BasicEvaluator basicEvaluator;

  @Before
  public void init() {
    basicEvaluator = new BasicEvaluator(new MathContext(100));
  }

  @Test
  public void testAbsoluteValue() {
    BigDecimal testNegative = new BigDecimal("-10.0");
    BigDecimal testPositive = new BigDecimal("10.0");
    String expected = "10.0";

    String result = basicEvaluator.absoluteValue(testNegative);
    Truth.assertThat(result).isEqualTo(expected);

    result = basicEvaluator.absoluteValue(testPositive);
    Truth.assertThat(result).isEqualTo(expected);
  }

  @Test
  public void testSquareRoot() {
    BigDecimal testPositive = new BigDecimal("144");
    String expected = "12";

    String result = basicEvaluator.squareRoot(testPositive);
    Truth.assertThat(result).isEqualTo(expected);
  }

  @Test
  public void testMax() {
    List<BigDecimal> testList =
        List.of(new BigDecimal("9.2"), new BigDecimal("33.5"), new BigDecimal("4902.3"));
    String expected = "4902.3";

    String result = basicEvaluator.max(testList);
    Truth.assertThat(result).isEqualTo(expected);
  }

  @Test
  public void testMin() {
    List<BigDecimal> testList =
        List.of(new BigDecimal("9.2"), new BigDecimal("33.5"), new BigDecimal("4902.3"));
    String expected = "9.2";

    String result = basicEvaluator.min(testList);
    Truth.assertThat(result).isEqualTo(expected);
  }
}
