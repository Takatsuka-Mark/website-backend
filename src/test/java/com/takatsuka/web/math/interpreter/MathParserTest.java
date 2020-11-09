package com.takatsuka.web.math.interpreter;

import com.google.common.truth.Truth;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.takatsuka.web.interpreter.Arg;
import com.takatsuka.web.interpreter.ExpressionEntry;
import com.takatsuka.web.interpreter.Function;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathParserTest {
  private MathParser mathParser;

  @Before
  public void init() {
    mathParser = new MathParser();
  }

  @Test
  public void testTokenize_simple(){
    String testString = "1 + 2";
    List<String> expectedTokens = List.of("1", "+", "2");

    List<String> fetchedTokens = mathParser.tokenize(testString);

    Truth.assertThat(expectedTokens).containsExactlyElementsIn(fetchedTokens);
  }

  @Test
  public void testLoadTokensIntoTables_singleExpression(){
    String testString = "1 + 2";
    List<String> testTokens = mathParser.tokenize(testString);
    ExpressionEntry expectedEntry =
        ExpressionEntry.newBuilder()
            .setLevel(1)
            .setId(1)
            .setFunction(Function.ADD)
            .addArgs(Arg.newBuilder().setArgValue("1").build())
            .addArgs(Arg.newBuilder().setArgValue("2").build())
            .build();
    Map<Integer, ExpressionEntry> expectedMap = Map.of(1, expectedEntry);

    Map<Integer, ExpressionEntry> fetchedMap = mathParser.loadTokensIntoTables(testTokens);

    ProtoTruth.assertThat(expectedMap).containsExactlyEntriesIn(fetchedMap);
  }

  @Test
  public void testFillSecondArguments() {
    ExpressionEntry entry1 =
        ExpressionEntry.newBuilder().addArgs(Arg.newBuilder().setArgValue("1")).build();
    ExpressionEntry expectedEntry1 =
        ExpressionEntry.newBuilder()
            .addArgs(Arg.newBuilder().setArgValue("1"))
            .addArgs(Arg.newBuilder().setArgValue("2"))
            .build();
    ExpressionEntry entry2 =
        ExpressionEntry.newBuilder()
            .addArgs(Arg.newBuilder().setArgValue("2"))
            .addArgs(Arg.newBuilder().setArgValue("3"))
            .build();

    Map<Integer, ExpressionEntry> testMap = Map.of(1, entry1, 2, entry2);
    Map<Integer, ExpressionEntry> fetchedMap = mathParser.fillSecondArguments(testMap);

    Map<Integer, ExpressionEntry> expectedMap = Map.of(1, expectedEntry1, 2, entry2);


    ProtoTruth.assertThat(fetchedMap).containsExactlyEntriesIn(expectedMap);
  }
}
