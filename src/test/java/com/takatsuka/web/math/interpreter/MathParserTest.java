package com.takatsuka.web.math.interpreter;

import com.google.common.truth.Truth;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.takatsuka.web.interpreter.ExpressionEntry;
import com.takatsuka.web.interpreter.Function;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MathParserTest {
  private MathParser mathParser;
  private final Map<Integer, ExpressionEntry> expectedExpressionMap =
      generateExpectedExpressionMap();
  private static final String SIMPLE_EXPRESSION = "1 + 2";
  private static final String MONO_VARIABLE_EXPRESSION = "2 * sqrt(24 / 3 + 1)";

  @Before
  public void init() {
    mathParser = new MathParser();
  }

  @Test
  public void testEvaluate_single() {
    //    Truth.assertThat(mathParser.evaluate("1")).isEqualTo(1.0);
  }

  @Test
  public void testEvaluate_simple() {
    Truth.assertThat(mathParser.evaluate(SIMPLE_EXPRESSION)).isEqualTo(3.0);
  }

  @Test
  public void testEvaluate_monoVariable() {
    Truth.assertThat(mathParser.evaluate(MONO_VARIABLE_EXPRESSION))
        .isEqualTo(2 * Math.sqrt((double) 24 / 3 + 1));
  }

  @Test
  public void testTokenize_simple() {
    List<String> expectedTokens = List.of("1", "+", "2");

    List<String> fetchedTokens = mathParser.tokenize(SIMPLE_EXPRESSION);

    Truth.assertThat(expectedTokens).containsExactlyElementsIn(fetchedTokens);
  }

  @Test
  public void testTokenize_monoVariable() {
    List<String> expectedTokens = List.of("2", "*", "sqrt", "(", "24", "/", "3", "+", "1", ")");

    List<String> fetchedTokens = mathParser.tokenize(MONO_VARIABLE_EXPRESSION);

    Truth.assertThat(expectedTokens).containsExactlyElementsIn(fetchedTokens);
  }

  @Test
  public void testLoadTokensIntoTables_singleExpression() {
    List<String> testTokens = mathParser.tokenize(SIMPLE_EXPRESSION);
    ExpressionEntry expectedEntry =
        ExpressionEntry.newBuilder()
            .setLevel(1)
            .setId(1)
            .setFunction(Function.ADD)
            .addArgs("1")
            .addArgs("2")
            .setMaxArg(2)
            .build();
    Map<Integer, ExpressionEntry> expectedMap = Map.of(1, expectedEntry);

    Map<Integer, ExpressionEntry> fetchedMap = mathParser.loadTokensIntoTables(testTokens);

    ProtoTruth.assertThat(expectedMap).containsExactlyEntriesIn(fetchedMap);
  }

  @Test
  public void testLoadTokensIntoTables_monoVariableFunction() {
    List<String> testTokens = mathParser.tokenize(MONO_VARIABLE_EXPRESSION);
    Map<Integer, ExpressionEntry> expectedMap =
        Map.of(
            1,
            ExpressionEntry.newBuilder()
                .setId(1)
                .setFunction(Function.MULTIPLY)
                .setMaxArg(2)
                .addArgs("2")
                .addArgs("0")
                .setLevel(2)
                .build(),
            2,
            ExpressionEntry.newBuilder()
                .setId(2)
                .setFunction(Function.SQUARE_ROOT)
                .setMaxArg(1)
                .addArgs("0")
                .addArgs("0")
                .setLevel(10)
                .build(),
            3,
            ExpressionEntry.newBuilder()
                .setId(3)
                .setFunction(Function.DIVIDE)
                .setMaxArg(2)
                .addArgs("24")
                .addArgs("0")
                .setLevel(12)
                .build(),
            4,
            ExpressionEntry.newBuilder()
                .setId(4)
                .setFunction(Function.ADD)
                .setMaxArg(2)
                .addArgs("3")
                .addArgs("1")
                .setLevel(11)
                .build());

    Map<Integer, ExpressionEntry> fetchedMap = mathParser.loadTokensIntoTables(testTokens);

    // TODO(mark): It turns out the fetchedMap doesn't contain the final function... Fix this
    ProtoTruth.assertThat(expectedMap).containsExactlyEntriesIn(fetchedMap);
  }

  @Test
  public void testFillSecondArguments_simple() {
    ExpressionEntry entry1 =
        ExpressionEntry.newBuilder().addArgs("1").addArgs("0").setMaxArg(2).build();
    ExpressionEntry expectedEntry1 =
        ExpressionEntry.newBuilder().addArgs("1").addArgs("2").setMaxArg(2).build();
    ExpressionEntry entry2 =
        ExpressionEntry.newBuilder().addArgs("2").addArgs("3").setMaxArg(2).build();

    Map<Integer, ExpressionEntry> testMap = Map.of(1, entry1, 2, entry2);
    Map<Integer, ExpressionEntry> fetchedMap = mathParser.fillSecondArguments(testMap);

    Map<Integer, ExpressionEntry> expectedMap = Map.of(1, expectedEntry1, 2, entry2);

    ProtoTruth.assertThat(fetchedMap).containsExactlyEntriesIn(expectedMap);
  }

  @Test
  public void testBuildRelations() {
    // This is the test from page 24 of clsMathParser
    Map<Integer, ExpressionEntry> inputMap =
        Map.of(
            1,
            ExpressionEntry.newBuilder()
                .setId(1)
                .setMaxArg(2)
                .setArgOf(0)
                .setArgId(0)
                .setLevel(2)
                .build(),
            2,
            ExpressionEntry.newBuilder()
                .setId(2)
                .setMaxArg(2)
                .setArgOf(0)
                .setArgId(0)
                .setLevel(11)
                .build(),
            3,
            ExpressionEntry.newBuilder()
                .setId(3)
                .setMaxArg(2)
                .setArgOf(0)
                .setArgId(0)
                .setLevel(2)
                .build(),
            4,
            ExpressionEntry.newBuilder()
                .setId(4)
                .setMaxArg(2)
                .setArgOf(0)
                .setArgId(0)
                .setLevel(11)
                .build(),
            5,
            ExpressionEntry.newBuilder()
                .setId(5)
                .setMaxArg(2)
                .setArgOf(0)
                .setArgId(0)
                .setLevel(1)
                .build());

    Map<Integer, ExpressionEntry> returnedMap = mathParser.buildRelations(inputMap);
    for (Map.Entry<Integer, ExpressionEntry> entry : expectedExpressionMap.entrySet()) {
      ProtoTruth.assertThat(entry.getValue()).isEqualTo(returnedMap.get(entry.getKey()));
    }
  }

  @Test
  public void testEvaluateMap_basic() {
    Map<Integer, ExpressionEntry> expressionMap =
        Map.of(
            1,
            ExpressionEntry.newBuilder()
                .setId(1)
                .setFunction(Function.ADD)
                .setMaxArg(2)
                .addArgs("2")
                .addArgs("5")
                .setArgOf(2)
                .setArgId(1)
                .build(),
            2,
            ExpressionEntry.newBuilder()
                .setId(2)
                .setFunction(Function.MULTIPLY)
                .setMaxArg(2)
                .addArgs("0") // Filler. It will be replaced
                .addArgs("9")
                .setArgOf(0)
                .setArgId(0)
                .build());
    List<Integer> sequenceList = List.of(1, 2);

    double result = mathParser.evaluateMap(expressionMap, sequenceList);
    Truth.assertThat(result).isEqualTo((double) (2 + 5) * 9);
  }

  private static Map<Integer, ExpressionEntry> generateExpectedExpressionMap() {
    return Map.of(
        1,
        ExpressionEntry.newBuilder()
            .setId(1)
            .setMaxArg(2)
            .setArgOf(3)
            .setArgId(1)
            .setLevel(2)
            .build(),
        2,
        ExpressionEntry.newBuilder()
            .setId(2)
            .setMaxArg(2)
            .setArgOf(1)
            .setArgId(2)
            .setLevel(11)
            .build(),
        3,
        ExpressionEntry.newBuilder()
            .setId(3)
            .setMaxArg(2)
            .setArgOf(5)
            .setArgId(1)
            .setLevel(2)
            .build(),
        4,
        ExpressionEntry.newBuilder()
            .setId(4)
            .setMaxArg(2)
            .setArgOf(3)
            .setArgId(2)
            .setLevel(11)
            .build(),
        5,
        ExpressionEntry.newBuilder()
            .setId(5)
            .setMaxArg(2)
            .setArgOf(0)
            .setArgId(0)
            .setLevel(1)
            .build());
  }
}
