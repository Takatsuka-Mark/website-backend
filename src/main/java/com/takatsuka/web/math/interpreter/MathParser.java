package com.takatsuka.web.math.interpreter;

import com.google.common.annotations.VisibleForTesting;
import com.takatsuka.web.interpreter.ExpressionEntry;
import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.MathSettings;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MathParser {
  private static final Logger logger = MathLogger.forCallingClass();

  private final Pattern regex;
  private final Evaluator evaluator;
  private final FunctionMapper functionMapper;

  public MathParser(FunctionMapper functionMapper) {
    this.functionMapper = functionMapper;
    regex = functionMapper.getPattern();
    evaluator = new Evaluator(functionMapper);
  }

  public String evaluate(String expression){
    return evaluate(expression, new MathSettings());
  }

  public String evaluate(String expression, MathSettings mathSettings) {
    ArrayList<String> tokens = tokenize(expression);
    return evaluate(tokens);
  }

  private String evaluate(List<String> tokens) {
    Map<Integer, ExpressionEntry> expressionTable1 = loadTokensIntoTables(tokens);
    Map<Integer, ExpressionEntry> expressionTable2 = fillSecondArguments(expressionTable1);
    Map<Integer, ExpressionEntry> expressionTable3 = buildRelations(expressionTable2);
    List<Integer> sequenceList = buildSequenceIndexList(expressionTable3);
    return evaluateMap(expressionTable3, sequenceList);
  }

  public ArrayList<String> tokenize(String expression) {
    Matcher m = regex.matcher(expression);

    ArrayList<String> tokens = new ArrayList<>();
    while (m.find()) {
      String tok = m.group();
      tokens.add(tok);
    }

    logger.debug("Expression '{}' successfully tokenized to '{}'", expression, tokens.toString());

    return tokens;
  }

  /** Step 1.0 */
  @VisibleForTesting
  protected Map<Integer, ExpressionEntry> loadTokensIntoTables(
      List<String> tokens) {
    Queue<String> argBackup = new LinkedList<>();
    HashMap<Integer, ExpressionEntry> expressionTable = new HashMap<>();
    int argPriority = 0;
    int funcId = 0;
    int outstandingParams = 0;

    for (int i = 0; i < tokens.size(); i++) {
      String token = tokens.get(i);
      if (token.matches("[(]")) {
        if (outstandingParams > 0) {
          // There must be a function present. Process and evaluate the args.
          int requiredClosingParen = 0;
          // Fetch the next tokens which need to be processed
          List<String> secondaryTokens = new ArrayList<>();
          do {
            String secondaryToken = tokens.get(i);
            secondaryTokens.add(secondaryToken);

            if (secondaryToken.matches("[(]")) {
              requiredClosingParen += 1;
            } else if (secondaryToken.matches("[)]")) {
              requiredClosingParen -= 1;
            } else if (secondaryToken.matches("[,]") && requiredClosingParen == 1) {
              // This is the end of the previous param. Evaluate it, and store it as a param
              secondaryTokens.remove(secondaryTokens.size() - 1); // Remove the last token ','
              String result = evaluate(secondaryTokens);
              expressionTable.put(
                  funcId, expressionTable.get(funcId).toBuilder().addArgs(result).build());
              // Reset the token params for the next token
              secondaryTokens.clear();
            }
            i += 1;
          } while (requiredClosingParen > 0);
          i -= 1;

          secondaryTokens.remove(secondaryTokens.size() - 1); // Remove the last token ')'
          String result = String.valueOf(evaluate(secondaryTokens));
          expressionTable.put(
              funcId, expressionTable.get(funcId).toBuilder().addArgs(result).build());

        } else {
          argPriority += 10;
        }
      } else if (token.matches("[)]")) {
        argPriority -= 10;
      } else {
        // It is not a paren.

        if (token.matches(functionMapper.getNumRegex())) {
          // It is a number.
          argBackup.add(token);
        } else {
          // It is some sort of function.
          funcId += 1;
          int level;
          Function function = functionMapper.mapStringToFunction(token);
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
              .setMaxArg(functionMapper.getMaxArgs(function));

          // We shouldn't have to make this call to FunctionMapper twice.
          if (functionMapper.isSymbolFunction(token)) {
            // This is of the form ' a [function] b '
            if (!argBackup.isEmpty()) {
              // Add it as another variable
              expressionBuilder.addArgs(argBackup.remove());
            }
          } else if (functionMapper.isMultiVariableFunction(token)) {
            // This is of the form ' [function]( a ) '
            outstandingParams += 1;
          }

          expressionTable.put(funcId, expressionBuilder.build());
        }
      }
    }

    // Make sure we don't bypass the final value.
    if (!argBackup.isEmpty()) {
      if (expressionTable.size() > 0) {
        // There must be a previous function. Add it as an arg.
        int idx = Collections.max(expressionTable.keySet());
        ExpressionEntry.Builder entryToUpdate = expressionTable.get(idx).toBuilder();

        if (functionMapper.getMaxArgs(entryToUpdate.getFunction()) != Integer.MAX_VALUE) {
          for (int j = entryToUpdate.getArgsCount(); j < entryToUpdate.getMaxArg() - 1; j++) {
            entryToUpdate.addArgs("0"); // Placeholder
          }
        }
        entryToUpdate.addArgs(argBackup.remove()); // Get the previous arg

        expressionTable.replace(idx, entryToUpdate.build());
      } else {
        // This is the only variable represented.
        funcId += 1;
        ExpressionEntry.Builder expressionBuilder = ExpressionEntry.newBuilder();
        expressionBuilder.setId(funcId);
        expressionBuilder.setLevel(argPriority + 1);
        expressionBuilder.addArgs(argBackup.remove());
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
      if (functionMapper.isSymbolFunction(expressions.get(i).getFunction())) {
        String newArg = expressions.get(i + 1).getArgs(0);
        resultMap.put(i, expressions.get(i).toBuilder().addArgs(newArg).build());
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
        if (biggestEntry.getMaxArg() != Integer.MAX_VALUE) {
          argId = biggestEntry.getMaxArg();
        } else {
          argId = biggestEntry.getArgsCount();
        }
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
  public String evaluateMap(Map<Integer, ExpressionEntry> expressionTable, List<Integer> sequence) {
    HashMap<Integer, ExpressionEntry> expressions = new HashMap<>(expressionTable);
    String finalValue = "";
    for (Integer index : sequence) {
      ExpressionEntry expressionEntry = expressions.get(index);
      finalValue =
          evaluator.evaluateFunction(expressionEntry.getFunction(), expressionEntry.getArgsList());
      int argOf = expressionEntry.getArgOf();
      int argId = expressionEntry.getArgId();

      // Ensure that we are only setting variables which need to be set, not just the value.
      if (argOf != 0) {
        ExpressionEntry.Builder expressionToUpdate = expressions.get(argOf).toBuilder();
        //        expressionToUpdate.setArgs(argId - 1, String.valueOf(finalValue));

        setArg(expressionToUpdate, argId - 1, finalValue);

        expressions.put(argOf, expressionToUpdate.build());
      }
    }
    return finalValue;
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

  private ExpressionEntry.Builder setArg(
      ExpressionEntry.Builder builder, int argPosition, String argValue) {
    if (argPosition < builder.getArgsCount()) {
      // It is safe to add it directly to the position
      builder.setArgs(argPosition, argValue);
    } else {
      for (int j = builder.getArgsCount(); j < argPosition; j++) {
        builder.addArgs("0"); // Placeholder
      }
      builder.addArgs(argValue);
    }

    return builder;
  }

//  private String padNumbers(String expression) {
//    boolean prevIsNum = false;
//    for (String tok: expression.split("")) {
//        if()
//    }
//  }
}
