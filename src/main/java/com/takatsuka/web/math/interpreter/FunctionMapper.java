package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.interpreter.FunctionDefinition;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.evaluators.EvaluatorGrouping;
import com.takatsuka.web.math.utils.MathMethod;
import com.takatsuka.web.utils.exceptions.MathParseException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;

public class FunctionMapper {
  private static final Logger logger = MathLogger.forCallingClass();

  private static final String SYMBOL_GROUPS = "([+\\-*/%^!()])";
  private static final String NUM_REGEX = "(((?<=[+\\-*`/%^(]|^)-)?\\d+([.]\\d+)?)";
  private static final String FUNC_REGEX = "(\\w+)";
  private static final String EVALUATOR_PACKAGE_PATH = "";

  private final Map<String, Function> multiVariableFunctionMap;
  private final Map<String, Function> symbolOperatorMap;
  private final Map<Function, Integer> functionToMaxArgMap;
  private final Map<Function, Method> functionToMethodMap;
  // private final Map<Function, List<ParamType>> functionToParamTypeMap;
  private final Map<Function, Pattern> functionToParamPattern;
  private final Map<String, Map<String, Function>> classMethodToFunction;
  private final Pattern pattern;

  public FunctionMapper(List<FunctionDefinition> functionsList) {
    multiVariableFunctionMap = new HashMap<>();
    symbolOperatorMap = new HashMap<>();
    functionToMaxArgMap = new HashMap<>();
    functionToMethodMap = new HashMap<>();
    // functionToParamTypeMap = new HashMap<>();
    functionToParamPattern = new HashMap<>();
    classMethodToFunction = new HashMap<>();
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
      // functionToMethodMap.put(functionDefinition.getFunction(), getMethod(functionDefinition));

      functionToParamPattern.put(
        functionDefinition.getFunction(), Pattern.compile(functionDefinition.getParamPattern())
      );
  
      // functionToParamTypeMap.put(
      //     functionDefinition.getFunction(), functionDefinition.getMathMethod().getParamTypesList());
      String className = functionDefinition.getMathMethod().getClassName();
      Map<String, Function> tmp = classMethodToFunction.getOrDefault(className, new HashMap<String, Function>());
      tmp.put(functionDefinition.getMathMethod().getMethodName(), functionDefinition.getFunction());
      classMethodToFunction.put(className, tmp);
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

  public Pattern getParamTypePattern(Function function) {
    return functionToParamPattern.get(function);
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

  // Maps the function name to list of Hash maps mapping arg pattern to the method
  public HashMap<Function, HashMap<String, Method>> mapEvalsToMethods(List<EvaluatorGrouping> evaluators) {
    HashMap<Function, HashMap<String, Method>> methodMap = new HashMap<>();
    for (EvaluatorGrouping eval : evaluators) {
      for (Method method : eval.getClass().getMethods()) {
        MathMethod method_annotation = method.getAnnotation(MathMethod.class);
        if (method_annotation == null) {
          // This method is not annotated. Skip
          continue;
        }
        Function function = this.classMethodToFunction.get(
          eval.getClass().getName().toString()
        ).get(
          method.getName()
        );

        if (function == null) {
          logger.warn(String.format(
            "Unable to map '%s' from class '%s'",
            method.getName(),
            eval.getClass().getName().toString()
          ));
          continue;
        }

        HashMap<String, Method> thisMethodMapping = methodMap.getOrDefault(function, new HashMap<>());

        String paramPattern = method_annotation.value();

        // TODO this is a bad method of handling overrides...
        // We should figure out some other way of combining the regexes...
        thisMethodMapping.put(paramPattern, method);
        methodMap.put(function, thisMethodMapping);
      }
    }

    return methodMap;
  }
}
