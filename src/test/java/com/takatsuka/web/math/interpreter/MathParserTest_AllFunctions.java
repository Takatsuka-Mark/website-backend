package com.takatsuka.web.math.interpreter;

import com.google.common.truth.Truth;
import com.takatsuka.web.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class MathParserTest_AllFunctions {

  private MathParser mathParser;

  @Before
  public void init() {
    FunctionMapper functionMapper = new FunctionMapper(FileUtils.loadFunctionsFromFiles());
    mathParser = new MathParser(functionMapper);
  }

  @Test
  public void testEvaluate_add() {
    String expectedValue = "68";
    String result = mathParser.evaluate("54 + 14");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_subtract() {
    String expectedValue = "7";
    String result = mathParser.evaluate("33 - 26");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_multiply() {
    String expectedValue = "969";
    String result = mathParser.evaluate("17 * 57");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_divide() {
    String expectedValue = "0.234375";
    String result = mathParser.evaluate("15 / 64");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_intDivide() {
    // Test round down
    String expectedValue = "1";
    String result = mathParser.evaluate("11 // 10");
    Truth.assertThat(result).isEqualTo(expectedValue);

    // Test lack of round up
    result = mathParser.evaluate("19 // 10");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_mod() {
    String expectedValue = "3";
    String result = mathParser.evaluate("43 % 20");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_power() {
    // TODO(mark): Implement
  }

  @Test
  public void testEvaluate_factorial() {
    // Test basic case
    String expectedValue = "120";
    String result = mathParser.evaluate("5!");
    Truth.assertThat(result).isEqualTo(expectedValue);

    // Test more complex param parsing.
    expectedValue = "121";
    result = mathParser.evaluate("5! + 1");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_absoluteValue() {
    String expectedValue = "69";
    String result = mathParser.evaluate("abs(-69)");
    String result2 = mathParser.evaluate("abs(69)");
    Truth.assertThat(result).isEqualTo(expectedValue);
    Truth.assertThat(result2).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_sine() {
    // TODO(mark): Implement
  }

  @Test
  public void testEvaluate_cosine() {
    // TODO(mark): Implement
  }

  @Test
  public void testEvaluate_squareRoot() {
    String expectedValue = "47";
    String result = mathParser.evaluate("sqrt(2209)");
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_max() {
    List<String> testList =
        List.of(60, 77, 3, 97, 49, 63, 25, 89, 52, 7).stream()
            .map(String::valueOf)
            .collect(Collectors.toList());
    String expectedValue = "97";
    String expression = "max(" + String.join(", ", testList) + ")";
    String result = mathParser.evaluate(expression);
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_min() {
    List<String> testList =
        List.of(60, 77, 3, 97, 49, 63, 25, 89, 52, 7).stream()
            .map(String::valueOf)
            .collect(Collectors.toList());
    String expectedValue = "3";
    String expression = "min(" + String.join(", ", testList) + ")";
    String result = mathParser.evaluate(expression);
    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_lg() {
    // TODO(mark): Implement this
    //    String expectedValue = "6";
    //    String result = mathParser.evaluate("lg(64)");
    //    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_log() {
    // TODO(mark): Implement this
    //    String expectedValue = "6";
    //    String result = mathParser.evaluate("log(1000000)");
    //    Truth.assertThat(result).isEqualTo(expectedValue);
  }

  @Test
  public void testEvaluate_logN() {}

  @Test
  public void testEvaluate_rng() {
    String expression = "rng(5, 100)";
    String result = mathParser.evaluate(expression);
    Truth.assertThat(Integer.parseInt(result)).isAtLeast(5);
    Truth.assertThat(Integer.parseInt(result)).isLessThan(100);
  }
}
