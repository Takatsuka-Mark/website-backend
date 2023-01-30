package com.takatsuka.web.math.interpreter;

import com.google.common.truth.Truth;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.takatsuka.web.interpreter.ExpressionEntry;
import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MathParserTest {
  private MathParser mathParser;
  private final Map<Integer, ExpressionEntry> expectedExpressionMap =
      generateExpectedExpressionMap();
  private static final String SIMPLE_EXPRESSION = "1 + 2 * 9";
  private static final String MONO_VARIABLE_EXPRESSION = "2 * sqrt(9) + sqrt(300 / 3)";
  private static final String MULTI_VARIABLE_EXPRESSION = "max(5, 20 / 2)";

  @Before
  public void init() {
    FunctionLoader functionLoader = new FunctionLoader(new FileUtils());
    FunctionMapper functionMapper = new FunctionMapper(functionLoader.loadFunctions());
    mathParser =
        new MathParser(
            functionMapper);
  }

  @Test
  public void testEvaluate_single() {
    // TODO(mark): '1)' fails, but '1' does not.
    Truth.assertThat(mathParser.evaluate("1")).isEqualTo("1");
  }

  @Test
  public void testEvaluate_negative() {
    Truth.assertThat(mathParser.evaluate("-1")).isEqualTo("-1");
  }

  @Test
  public void testEvaluate_simple() {
    Truth.assertThat(mathParser.evaluate(SIMPLE_EXPRESSION)).isEqualTo("19");
  }

  @Test
  public void testEvaluate_monoVariable() {
    Truth.assertThat(mathParser.evaluate(MONO_VARIABLE_EXPRESSION)).isEqualTo("16");
  }

  @Test
  public void testEvaluate_multiVariable_max2() {
    Truth.assertThat(mathParser.evaluate(MULTI_VARIABLE_EXPRESSION)).isEqualTo("10");
  }

  @Test
  public void testTokenize_simple() {
    List<String> expectedTokens = List.of("1", "+", "2", "*", "9");

    List<String> fetchedTokens = mathParser.tokenize(SIMPLE_EXPRESSION);

    Truth.assertThat(expectedTokens).containsExactlyElementsIn(fetchedTokens);
  }

  @Test
  public void testTokenize_monoVariable() {
    List<String> expectedTokens =
        List.of("2", "*", "sqrt", "(", "9", ")", "+", "sqrt", "(", "300", "/", "3", ")");

    List<String> fetchedTokens = mathParser.tokenize(MONO_VARIABLE_EXPRESSION);

    Truth.assertThat(expectedTokens).containsExactlyElementsIn(fetchedTokens);
  }

  @Test
  public void testTokenize_multiVariable() {
    List<String> expectedTokens = List.of("max", "(", "5", ",", "20", "/", "2", ")");

    List<String> fetchedTokens = mathParser.tokenize(MULTI_VARIABLE_EXPRESSION);

    Truth.assertThat(expectedTokens).containsExactlyElementsIn(fetchedTokens);
  }

  @Test
  public void testTokenize_noSpace() {
    List<String> expectedTokens = List.of("1", "+", "2");

    List<String> fetchedTokens = mathParser.tokenize("1+2");

    Truth.assertThat(fetchedTokens).containsExactlyElementsIn(expectedTokens);
  }

  @Test
  public void testLoadTokensIntoTables_simpleExpression() {
    List<String> testTokens = mathParser.tokenize(SIMPLE_EXPRESSION);
    Map<Integer, ExpressionEntry> expectedMap =
        Map.of(
            1,
            ExpressionEntry.newBuilder()
                .setLevel(1)
                .setId(1)
                .setFunction(Function.ADD)
                .addArgs("1")
                .setMaxArg(2)
                .build(),
            2,
            ExpressionEntry.newBuilder()
                .setLevel(2)
                .setId(2)
                .setFunction(Function.MULTIPLY)
                .addArgs("2")
                .addArgs("9")
                .setMaxArg(2)
                .build());

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
                .setLevel(2)
                .build(),
            2,
            ExpressionEntry.newBuilder()
                .setId(2)
                .setFunction(Function.SQUARE_ROOT)
//                .setMaxArg(1)
                .addArgs("9")
                .setLevel(10)
                .build(),
            3,
            ExpressionEntry.newBuilder()
                .setId(3)
                .setFunction(Function.ADD)
                .setMaxArg(2)
                .setLevel(1)
                .build(),
            4,
            ExpressionEntry.newBuilder()
                .setId(4)
                .setFunction(Function.SQUARE_ROOT)
//                .setMaxArg(1)
                .addArgs("100")
                .setLevel(10)
                .build());

    Map<Integer, ExpressionEntry> fetchedMap = mathParser.loadTokensIntoTables(testTokens);

    ProtoTruth.assertThat(fetchedMap).containsExactlyEntriesIn(expectedMap);
  }

  @Test
  public void testFillSecondArguments_simple() {
    List<String> tokens = mathParser.tokenize(SIMPLE_EXPRESSION);
    Map<Integer, ExpressionEntry> tokenMap = mathParser.loadTokensIntoTables(tokens);
    Map<Integer, ExpressionEntry> filledSecondArguments = mathParser.fillSecondArguments(tokenMap);

    Map<Integer, ExpressionEntry> expectedArgs =
        Map.of(1, tokenMap.get(1).toBuilder().addArgs("2").build(), 2, tokenMap.get(2));

    ProtoTruth.assertThat(expectedArgs).containsExactlyEntriesIn(filledSecondArguments);
  }

  @Test
  public void testFillSecondArguments_monoVariableFunction() {
    List<String> tokens = mathParser.tokenize(MONO_VARIABLE_EXPRESSION);
    Map<Integer, ExpressionEntry> tokenMap = mathParser.loadTokensIntoTables(tokens);
    Map<Integer, ExpressionEntry> filledSecondArguments = mathParser.fillSecondArguments(tokenMap);

    // TODO: working, but implement the test anyway
    // TODO: Also, could just make the map once and then match but ignore missing args
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
  public void testBuildRelations_monoVariableFunction() {
    List<String> tokens = mathParser.tokenize(MONO_VARIABLE_EXPRESSION);
    Map<Integer, ExpressionEntry> tokenMap = mathParser.loadTokensIntoTables(tokens);
    Map<Integer, ExpressionEntry> filledSecondArguments = mathParser.fillSecondArguments(tokenMap);
    Map<Integer, ExpressionEntry> builtRelations = mathParser.buildRelations(filledSecondArguments);

    // TODO(Mark): Actually assert something here about the relations.
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
                .addArgs("1")
                .addArgs("2")
                .setArgOf(0)
                .setArgId(0)
                .build(),
            2,
            ExpressionEntry.newBuilder()
                .setId(2)
                .setFunction(Function.MULTIPLY)
                .setMaxArg(2)
                .addArgs("2")
                .addArgs("9")
                .setArgOf(1)
                .setArgId(2)
                .build());
    List<Integer> sequenceList = List.of(2, 1);

    String result = mathParser.evaluateMap(expressionMap, sequenceList);
    Truth.assertThat(result).isEqualTo("19");
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
