package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Arg;
import com.takatsuka.web.interpreter.ExpressionEntry;
import com.takatsuka.web.interpreter.Function;
import javassist.expr.Expr;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MathParser {

  private Pattern regex = FunctionMapper.getPattern();

  public MathParser() {}

  public ArrayList<String> tokenize(String expression) {
    // TODO could look for only positives, and interpret '-' as * -1
    Matcher m = regex.matcher(expression);

    ArrayList<String> tokens = new ArrayList<>();
    while (m.find()) {
      String tok = m.group();
      tokens.add(tok);
    }

    return tokens;
  }

  /** Step 1.0 */
  public Map<Integer, ExpressionEntry> loadTokensIntoTables(List<String> tokens) {
    Queue<String> argBackup = new LinkedList<>();
    HashMap<Integer, ExpressionEntry> expressionTable = new HashMap<>();
    int argPriority = 0;
    int funcId = 0;
    int tokenNum = 0;

    for (String token : tokens) {
      tokenNum += 1;
      if (token.matches("[(]")) {
        argPriority += 10;
      } else if (token.matches("[)]")) {
        argPriority -= 10;
      } else {
        // It is not a paren.

        if (token.matches(FunctionMapper.getNumRegex())) {
          // It is a number.
          argBackup.add(token);
        } else {
          // it is a function.
          funcId += 1;
          int level;
          Function function = FunctionMapper.mapStringToFunction(token);
          switch (Objects.requireNonNull(function)) {
            case ADD:
            case SUBTRACT:
              level = 1;
              break;
            case MULTIPLY:
            case DIVIDE:
              level = 2;
              break;
            case POWER:
              level = 3;
              break;
            default:
              level = 10;
              break;
          }

          ExpressionEntry.Builder expressionBuilder = ExpressionEntry.newBuilder();
          expressionBuilder.setId(funcId);
          expressionBuilder.setFunction(function);
          expressionBuilder.addArgs(
              Arg.newBuilder().setArgValue(new BigDecimal(argBackup.remove()).toString()));
          expressionBuilder.setLevel(level + argPriority);
          expressionTable.put(funcId, expressionBuilder.build());
        }
      }
    }

    // Make sure we don't bypass the final value.
    if (!argBackup.isEmpty()) {
      int idx = expressionTable.size();
      expressionTable.replace(
          idx,
          expressionTable.get(idx).toBuilder()
              .addArgs(
                  Arg.newBuilder()
                      .setArgValue(new BigDecimal(argBackup.remove()).toString())
                      .build())
              .build());
    }
    return expressionTable;
  }

  /** Step 2.0 */
  public Map<Integer, ExpressionEntry> fillSecondArguments(
      Map<Integer, ExpressionEntry> expressions) {
    // Run through in reverse order, so every expression's 2nd arg is the 1st of the one after it
    HashMap<Integer, ExpressionEntry> resultMap = new HashMap<>();

    int size = expressions.size();
    for (int i = 1; i < size; i++) {
      Arg newArg = expressions.get(i + 1).getArgs(0);
      resultMap.put(i, expressions.get(i).toBuilder().addArgs(newArg).build());
    }
    resultMap.put(size, expressions.get(size));

    return resultMap;
  }

  /** Step 3.0 */
  public Map<Integer, ExpressionEntry> buildRelations(Map<Integer, ExpressionEntry> expressions) {
    return null;
  }

  public String evaluateTable(ArrayList<ExpressionEntry> expressionTable) {
    return "Waka waka ";
  }
}
