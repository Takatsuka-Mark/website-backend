package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class FunctionMapper {

  private static final Map<String, Function> MONO_VARIABLE_FUNCTION_MAP =
      generateMonoVariableFunctionMap();
  private static final Map<String, Function> SYMBOL_OPERATOR_MAP = generateSymbolOperatorMap();
  private static final Map<Function, Integer> FUNCTION_TO_MAX_ARG_MAP =
      generateFunctionToMaxArgsMap();

  private static final String SymbolGroups = "[+\\-*/%^!()]";
  private static final String numRegex = "-?\\d+(.\\d+)?";

  public static Function mapStringToFunction(String token) {
    if (MONO_VARIABLE_FUNCTION_MAP.containsKey(token)) {
      return MONO_VARIABLE_FUNCTION_MAP.get(token);
    }
    if (SYMBOL_OPERATOR_MAP.containsKey(token)) {
      return SYMBOL_OPERATOR_MAP.get(token);
    }
    return null;
  }

  public static int mapFunctionToMaxArgs(Function function) {
    return FUNCTION_TO_MAX_ARG_MAP.get(function);
  }

  public static boolean isMonoVariableFunction(String symbol) {
    return MONO_VARIABLE_FUNCTION_MAP.containsKey(symbol);
  }

  public static boolean isSymbolFunction(String symbol) {
    return SYMBOL_OPERATOR_MAP.containsKey(symbol);
  }

  public static boolean isSymbolFunction(Function function) {
    return SYMBOL_OPERATOR_MAP.containsValue(function);
  }

  // TODO(mark) Support non-spaced expressions
  private static Map<String, Function> generateMonoVariableFunctionMap() {
    HashMap<String, Function> tmp = new HashMap<>();
    tmp.put("abs", Function.ABSOLUTE_VALUE);
    tmp.put("sin", Function.SINE);
    tmp.put("cos", Function.COSINE);
    tmp.put("sqrt", Function.SQUARE_ROOT);
    return tmp;
  }

  private static Map<String, Function> generateSymbolOperatorMap() {
    HashMap<String, Function> tmp = new HashMap<>();
    tmp.put("+", Function.ADD);
    tmp.put("-", Function.SUBTRACT);
    tmp.put("*", Function.MULTIPLY);
    tmp.put("/", Function.DIVIDE);
    tmp.put("%", Function.MOD);
    tmp.put("!", Function.FACTORIAL);
    return tmp;
  }

  public static Set<String> getAllSymbols() {
    Set<String> tmp = new HashSet<>();
    tmp.addAll(MONO_VARIABLE_FUNCTION_MAP.keySet());
    tmp.addAll(SYMBOL_OPERATOR_MAP.keySet());
    return tmp;
  }

  public static Pattern getPattern() {
    ArrayList<String> patterns = new ArrayList<>();
    patterns.add(numRegex);
    patterns.add(SymbolGroups);
    patterns.addAll(MONO_VARIABLE_FUNCTION_MAP.keySet());

    return Pattern.compile(String.join("|", patterns));
  }

  public static String getNumRegex() {
    return numRegex;
  }

  public static Map<Function, Integer> generateFunctionToMaxArgsMap() {
    HashMap<Function, Integer> tmp = new HashMap<>();
    tmp.put(Function.ADD, 2);
    tmp.put(Function.SUBTRACT, 2);
    tmp.put(Function.MULTIPLY, 2);
    tmp.put(Function.DIVIDE, 2);
    tmp.put(Function.INT_DIVIDE, 2);
    tmp.put(Function.MOD, 2);
    tmp.put(Function.POWER, 2);
    tmp.put(Function.FACTORIAL, 1);
    tmp.put(Function.ABSOLUTE_VALUE, 1);
    tmp.put(Function.SINE, 1);
    tmp.put(Function.COSINE, 1);
    tmp.put(Function.SQUARE_ROOT, 1);
    return tmp;
  }
}
