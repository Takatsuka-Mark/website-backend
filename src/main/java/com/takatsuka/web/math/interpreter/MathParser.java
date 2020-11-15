package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.ExpressionEntry;
import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.logging.MathLogger;
import org.apache.el.lang.ExpressionBuilder;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MathParser {
  private static final Logger logger = MathLogger.forCallingClass();

  private final Pattern regex = FunctionMapper.getPattern();

  public MathParser() {}

  public double evaluate(String expression) {
    ArrayList<String> tokens = tokenize(expression);
    Map<Integer, ExpressionEntry> expressionTable1 = loadTokensIntoTables(tokens);
    Map<Integer, ExpressionEntry> expressionTable2 = fillSecondArguments(expressionTable1);
    Map<Integer, ExpressionEntry> expressionTable3 = buildRelations(expressionTable2);
    List<Integer> sequenceList = buildSequenceIndexList(expressionTable3);
    return evaluateMap(expressionTable3, sequenceList);
  }

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

  public Map<Integer, ExpressionEntry> loadTokensIntoTables(List<String> tokens) {
    return loadTokensIntoTables(tokens, 0, 0);
  }

  /** Step 1.0 */
  private Map<Integer, ExpressionEntry> loadTokensIntoTables(
      List<String> tokens, int currentFuncId, int currentPriority) {
    Queue<String> argBackup = new LinkedList<>();
    HashMap<Integer, ExpressionEntry> expressionTable = new HashMap<>();
    int argPriority = currentPriority;
    int funcId = currentFuncId;
    int outstandingParams = 0;

    for (int i = 0; i < tokens.size(); i++) {
      String token = tokens.get(i);
      if (token.matches("[(]")) {
        if (outstandingParams > 0) {
          int requiredClosingParen = 0;
          // Fetch the next tokens which need to be processed
          List<String> secondaryTokens = new ArrayList<>();
          do {
            String secondaryToken = tokens.get(i);
            secondaryTokens.add(secondaryToken);
            if (secondaryToken.matches("[(]")) requiredClosingParen += 1;
            if (secondaryToken.matches("[)]")) requiredClosingParen -= 1;
            i += 1;
          } while (requiredClosingParen > 0);
          i -= 1;

          // TODO should not join, but start already tokenized.
          String result = String.valueOf(evaluate(String.join(" ", secondaryTokens)));
          expressionTable.put(
              funcId, expressionTable.get(funcId).toBuilder().setArgs(0, result).build());
        } else {
          argPriority += 10;
        }
      } else if (token.matches("[)]")) {
        argPriority -= 10;
      } else {
        // It is not a paren.

        if (token.matches(FunctionMapper.getNumRegex())) {
          // It is a number.
          argBackup.add(token);
        } else {
          // It is some sort of function.
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
          expressionBuilder
              .setId(funcId)
              .setLevel(level + argPriority)
              .setFunction(function)
              .setMaxArg(FunctionMapper.mapFunctionToMaxArgs(function));

          // We shouldn't have to make this call to FunctionMapper twice.
          if (FunctionMapper.isSymbolFunction(token)) {
            // This is of the form ' a [function] b '
            if (!argBackup.isEmpty()) {
              expressionBuilder.addArgs(argBackup.remove()); // Get the previous arg
            } else {
              expressionBuilder.addArgs("0");
            }
            expressionBuilder.addArgs("0");
          } else if (FunctionMapper.isMonoVariableFunction(token)) {
            // This is of the form ' [function]( a ) '
            outstandingParams += FunctionMapper.mapFunctionToMaxArgs(function);
            expressionBuilder.addArgs("0");
          }

          expressionTable.put(funcId, expressionBuilder.build());
        }
      }
    }

    // Make sure we don't bypass the final value.
    if (!argBackup.isEmpty()) {
      if (expressionTable.size() > 0) {
        // There must be a previous function

        int idx = Collections.max(expressionTable.keySet());
        expressionTable.replace(
            idx,
            expressionTable.get(idx).toBuilder()
                .setArgs(expressionTable.get(idx).getMaxArg() - 1, argBackup.remove())
                .build());
      } else {
        // This is the only variable represented.
        // TODO do this a different way
        funcId += 1;
        ExpressionEntry.Builder expressionBuilder = ExpressionEntry.newBuilder();
        expressionBuilder.setId(funcId);
        expressionBuilder.setLevel(argPriority + 1);
        expressionBuilder.addArgs(argBackup.remove());
        //        expressionBuilder.setFunction(Function.ADD);
        //        expressionBuilder.addArgs("0");
        //        expressionBuilder.setMaxArg(2);
        expressionBuilder.setMaxArg(1);
        expressionTable.put(funcId, expressionBuilder.build());
      }
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
      String newArg = expressions.get(i + 1).getArgs(0);
      if (FunctionMapper.isSymbolFunction(expressions.get(i).getFunction())) {
        resultMap.put(i, expressions.get(i).toBuilder().setArgs(1, newArg).build());
      } else {
        resultMap.put(i, expressions.get(i));
      }
    }
    resultMap.put(size, expressions.get(size));

    return resultMap;
  }

  /** Step 3.0 */
  public Map<Integer, ExpressionEntry> buildRelations(Map<Integer, ExpressionEntry> expressions) {
    HashMap<Integer, ExpressionEntry> relationMap = new HashMap<>();
    HashMap<Integer, ExpressionEntry> remainingMap = new HashMap<>(expressions);
    int mapSize = remainingMap.size();

    // Continue while there are > 1 ArgOf(0)
    while (countArgOf0(remainingMap) > 1) {
      int thisLevel = getMaxLevelIdWhereArgOf0(remainingMap);
      ExpressionEntry thisEntry = remainingMap.get(thisLevel);
      ExpressionEntry prevEntry = null;
      ExpressionEntry nextEntry = null;
      ExpressionEntry biggestEntry = null;
      int argId = 1;

      // Iterate to find previous and next entry
      for (int i = thisLevel - 1; i > 0; i--) {
        if (remainingMap.containsKey(i)) {
          prevEntry = remainingMap.get(i);
          break;
        }
      }
      for (int i = thisLevel + 1; i <= mapSize; i++) {
        if (remainingMap.containsKey(i)) {
          nextEntry = remainingMap.get(i);
          break;
        }
      }

      // Collect the entry with the largest level.
      if (prevEntry != null) {
        if (nextEntry != null) {
          if (prevEntry.getLevel() >= nextEntry.getLevel()) {
            biggestEntry = prevEntry;
          } else {
            biggestEntry = nextEntry;
          }
        } else {
          biggestEntry = prevEntry;
        }
      } else {
        biggestEntry = nextEntry;
      }

      // Add this relation to the relation map, and remove it from the remainingMap
      if (biggestEntry.getId() < thisEntry.getId()) {
        argId = biggestEntry.getMaxArg();
      }
      relationMap.put(
          thisLevel, thisEntry.toBuilder().setArgOf(biggestEntry.getId()).setArgId(argId).build());
      remainingMap.remove(thisLevel);
    }

    // There should be a single value left, so we add this to the relation map.
    ExpressionEntry finalEntry = new ArrayList<>(remainingMap.values()).get(0);
    relationMap.put(finalEntry.getId(), finalEntry);

    return relationMap;
  }

  /** Parser step 4.0 */
  public List<Integer> buildSequenceIndexList(Map<Integer, ExpressionEntry> expressions) {
    List<Integer> sequence = new ArrayList<>();
    HashMap<Integer, ExpressionEntry> remainingExpressions = new HashMap<>(expressions);

    while (remainingExpressions.size() > 0) {
      int maxLevelIdx = getMaxLevel(remainingExpressions);
      sequence.add(maxLevelIdx);
      remainingExpressions.remove(maxLevelIdx);
    }

    return sequence;
  }

  /** Parser step 6.0 (no its not 5.0) */
  public double evaluateMap(Map<Integer, ExpressionEntry> expressionTable, List<Integer> sequence) {
    HashMap<Integer, ExpressionEntry> expressions = new HashMap<>(expressionTable);
    double finalValue = 0.0D;
    for (Integer index : sequence) {
      ExpressionEntry expressionEntry = expressions.get(index);
      finalValue = evaluateFunction(expressionEntry.getFunction(), expressionEntry.getArgsList());
      int argOf = expressionEntry.getArgOf();
      int argId = expressionEntry.getArgId();

      if (argOf != 0) {
        ExpressionEntry expressionToUpdate = expressions.get(argOf);
        List<String> argsToUpdate = expressionToUpdate.getArgsList();
        // Work with the ID - 1 because the id is indexed at 1.
        expressions.put(
            argOf,
            expressionToUpdate.toBuilder().setArgs(argId - 1, String.valueOf(finalValue)).build());
      }
    }
    return finalValue;
  }

  public double evaluateFunction(Function function, List<String> args) {
    switch (function) {
      case UNKNOWN_FUNCTION:
        // The function is unknown, just return the args if available.
        return Double.parseDouble(args.get(0));

        // Symbol Functions (Excluding FAC)
      case ADD:
        return Double.parseDouble(args.get(0)) + Double.parseDouble(args.get(1));
      case SUBTRACT:
        return Double.parseDouble(args.get(0)) - Double.parseDouble(args.get(1));
      case MULTIPLY:
        return Double.parseDouble(args.get(0)) * Double.parseDouble(args.get(1));
      case DIVIDE:
        return Double.parseDouble(args.get(0)) / Double.parseDouble(args.get(1));
      case MOD:
        return Double.parseDouble(args.get(0)) % Double.parseDouble(args.get(1));

        // Mono Variable Functions (Excluding FAC);
      case ABSOLUTE_VALUE:
        return Math.abs(Double.parseDouble(args.get(0)));
      case SQUARE_ROOT:
        return Math.sqrt(Double.parseDouble(args.get(0)));
      case SINE:
        return Math.sin(Double.parseDouble(args.get(0)));
      case COSINE:
        return Math.cos(Double.parseDouble(args.get(0)));
    }
    return 0.0D;
  }

  private int countArgOf0(Map<Integer, ExpressionEntry> expressions) {
    int acc = 0;
    for (ExpressionEntry entry : expressions.values()) {
      if (entry.getArgOf() == 0) acc += 1;
    }
    return acc;
  }

  private int getMaxLevelIdWhereArgOf0(Map<Integer, ExpressionEntry> expressions) {
    int currentMaxValue = -1;
    int currentMaxId = -1;
    for (Map.Entry<Integer, ExpressionEntry> entry : expressions.entrySet()) {
      if (entry.getValue().getArgOf() == 0 && entry.getValue().getLevel() > currentMaxValue) {
        currentMaxValue = entry.getValue().getLevel();
        currentMaxId = entry.getKey();
      }
    }

    return currentMaxId;
  }

  private int getMaxLevel(Map<Integer, ExpressionEntry> expressions) {
    int currentMaxValue = -1;
    int currentMaxId = -1;

    for (Map.Entry<Integer, ExpressionEntry> entry : expressions.entrySet()) {
      if (entry.getValue().getLevel() > currentMaxValue) {
        currentMaxValue = entry.getValue().getLevel();
        currentMaxId = entry.getKey();
      }
    }

    return currentMaxId;
  }
}
