package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class FunctionMapper {

  private static final Map<String, Function> SYMBOL_FUNCTION_MAP = generateSymbolFunctionMap();
  private static final Map<String, Function> SYMBOL_OPERATOR_MAP = generateSymbolOperatorMap();

  private static final String SymbolGroups = "[+\\-*/%^!()]";
  private static final String numRegex = "-?\\d+(.\\d+)?";

  public static Function mapStringToFunction(String token){
    if(SYMBOL_FUNCTION_MAP.containsKey(token)){
      return SYMBOL_FUNCTION_MAP.get(token);
    }
    if(SYMBOL_OPERATOR_MAP.containsKey(token)){
      return SYMBOL_OPERATOR_MAP.get(token);
    }
    return null;
  }

  // TODO(mark) Support non-spaced expressions
  private static Map<String, Function> generateSymbolFunctionMap() {
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
    tmp.addAll(SYMBOL_FUNCTION_MAP.keySet());
    tmp.addAll(SYMBOL_OPERATOR_MAP.keySet());
    return tmp;
  }

  public static Pattern getPattern() {
    ArrayList<String> patterns = new ArrayList<>();
    patterns.add(numRegex);
    patterns.add(SymbolGroups);
    patterns.addAll(SYMBOL_FUNCTION_MAP.keySet());

    return Pattern.compile(String.join("|", patterns));
  }

  public static String getNumRegex() {
    return numRegex;
  }
}
