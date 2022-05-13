package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.interpreter.FunctionDefinition;
import com.takatsuka.web.interpreter.ParamType;
import com.takatsuka.web.utils.exceptions.MathParseException;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class FunctionMapper {
  private static final String SYMBOL_GROUPS = "([+\\-*/%^!()])";
  private static final String NUM_REGEX = "(((?<=[+\\-*`/%^(]|^)-)?\\d+([.]\\d+)?)";
  private static final String FUNC_REGEX = "(\\w+)";

  private final Map<String, Function> multiVariableFunctionMap;
  private final Map<String, Function> symbolOperatorMap;
  private final Map<Function, Integer> functionToMaxArgMap;
  private final Map<Function, Method> functionToMethodMap;
  private final Map<Function, List<ParamType>> functionToParamTypeMap;
  private final Pattern pattern;

  public FunctionMapper(List<FunctionDefinition> functionsList) {
    multiVariableFunctionMap = new HashMap<>();
    symbolOperatorMap = new HashMap<>();
    functionToMaxArgMap = new HashMap<>();
    functionToMethodMap = new HashMap<>();
    functionToParamTypeMap = new HashMap<>();
    ArrayList<String> patterns = new ArrayList<>();
    patterns.add("//"); // Add integer division as so it is not parsed as two '/'
    patterns.add(NUM_REGEX);
    patterns.add(SYMBOL_GROUPS);
    patterns.add(FUNC_REGEX);
    patterns.add(",");

    // Build multi-variable functions
    for (FunctionDefinition functionDefinition : functionsList) {
      // Map the symbol to the function
      multiVariableFunctionMap.put(
          functionDefinition.getSymbol().toLowerCase(), functionDefinition.getFunction());

      // Map the function to max-args.
      functionToMaxArgMap.put(functionDefinition.getFunction(), functionDefinition.getMaxArgs());

      // Add function value
      functionToMethodMap.put(functionDefinition.getFunction(), getMethod(functionDefinition));

      functionToParamTypeMap.put(
          functionDefinition.getFunction(), functionDefinition.getMathMethod().getParamTypesList());
    }

    // Build operators
    symbolOperatorMap.put("//", Function.INT_DIVIDE);
    functionToMaxArgMap.put(Function.INT_DIVIDE, 2);
    symbolOperatorMap.put("+", Function.ADD);
    functionToMaxArgMap.put(Function.ADD, 2);
    symbolOperatorMap.put("-", Function.SUBTRACT);
    functionToMaxArgMap.put(Function.SUBTRACT, 2);
    symbolOperatorMap.put("*", Function.MULTIPLY);
    functionToMaxArgMap.put(Function.MULTIPLY, 2);
    symbolOperatorMap.put("/", Function.DIVIDE);
    functionToMaxArgMap.put(Function.DIVIDE, 2);
    symbolOperatorMap.put("%", Function.MOD);
    functionToMaxArgMap.put(Function.MOD, 2);
    symbolOperatorMap.put("^", Function.POWER);
    functionToMaxArgMap.put(Function.POWER, 2);
    symbolOperatorMap.put("!", Function.FACTORIAL); // TODO(Mark): Added this.
    functionToMaxArgMap.put(Function.FACTORIAL, 1);

    pattern = Pattern.compile(String.join("|", patterns));
  }

  public List<ParamType> getParamTypeList(Function function) {
    return functionToParamTypeMap.get(function);
  }

  public Map<Function, Method> getFunctionToMethodMap() {
    return functionToMethodMap;
  }

  public Function mapStringToFunction(String token) {
    token = token.toLowerCase();
    if (multiVariableFunctionMap.containsKey(token)) {
      return multiVariableFunctionMap.get(token);
    }
    if (symbolOperatorMap.containsKey(token)) {
      return symbolOperatorMap.get(token);
    }
    throw new MathParseException(token, null, MathParseException.ParseExceptionType.FUNCTION_NOT_DEFINED);
  }

  public int getMaxArgs(Function function) {
    return functionToMaxArgMap.get(function);
  }

  public boolean isMultiVariableFunction(String symbol) {
    return multiVariableFunctionMap.containsKey(symbol);
  }

  public boolean isMultiVariableFunction(Function function) {
    return multiVariableFunctionMap.containsValue(function);
  }

  public boolean isSymbolFunction(String symbol) {
    return symbolOperatorMap.containsKey(symbol);
  }

  public boolean isSymbolFunction(Function function) {
    return symbolOperatorMap.containsValue(function);
  }

  public Set<String> getAllSymbols() {
    Set<String> tmp = new HashSet<>();
    tmp.addAll(multiVariableFunctionMap.keySet());
    tmp.addAll(symbolOperatorMap.keySet());
    return tmp;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public String getNumRegex() {
    return NUM_REGEX;
  }

  private Method getMethod(FunctionDefinition functionDefinition) {
    try {
      List<Class> args = new ArrayList<>();
      Class<?> c = Class.forName(functionDefinition.getMathMethod().getClassName());

      for (ParamType param : functionDefinition.getMathMethod().getParamTypesList()) {
        switch (param) {
          case DECIMAL:
            args.add(Double.class);
            break;
          case INTEGER:
            args.add(Integer.class);
            break;
          case BIG_DECIMAL:
            args.add(BigDecimal.class);
            break;
          case BIG_INTEGER:
            args.add(BigInteger.class);
            break;
          case INTEGER_LIST:
          case DECIMAL_LIST:
          case BIG_INTEGER_LIST:
          case BIG_DECIMAL_LIST:
            args.add(List.class);
            break;
        }
      }

      return c.getDeclaredMethod(
          functionDefinition.getMathMethod().getMethodName(), args.toArray(Class[]::new));
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }

    return null;
  }
}
