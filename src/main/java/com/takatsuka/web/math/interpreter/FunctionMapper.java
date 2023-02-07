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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;

public class FunctionMapper {
  private static final Logger logger = MathLogger.forCallingClass();

  private static final String SYMBOL_GROUPS = "([!()])";
  private static final String NUM_REGEX = "(-?\\d+(\\.\\d+)?)";
  private static final Pattern NUM_PATTERN = Pattern.compile(NUM_REGEX);
  private static final String FUNC_REGEX = "(\\w+)";
  private static final String EVALUATOR_PACKAGE_PATH = "";
  
  private final Map<String, Function> multiVariableFunctionMap;
  private final Map<String, Function> symbolOperatorMap;
  private final Map<Pattern, Function> patternOperatorMap;
  private final Map<String, Map<String, Function>> classMethodToFunction;
  private final Pattern pattern;

  private final Map<Function, FunctionDefinition> functionToDefinition;

  public FunctionMapper(List<FunctionDefinition> functionsList) {
    multiVariableFunctionMap = new HashMap<>();
    symbolOperatorMap = new HashMap<>();
    classMethodToFunction = new HashMap<>();
    patternOperatorMap = new HashMap<>();
    functionToDefinition = new HashMap<>();
    ArrayList<String> patterns = new ArrayList<>();

    patterns.add(FUNC_REGEX);
    patterns.add(",");

    // Build multi-variable functions
    for (FunctionDefinition functionDefinition : functionsList) {
      functionToDefinition.put(functionDefinition.getFunction(), functionDefinition);

      // Map the symbol to the function
      multiVariableFunctionMap.put(
          functionDefinition.getSymbol().toLowerCase(), functionDefinition.getFunction());

      // Add function value
      if (functionDefinition.getIsInPlace()) {
        if (!functionDefinition.getRegex().equals("")) {
          patterns.add(functionDefinition.getRegex());
        } else {
          patterns.add(functionDefinition.getSymbol());
        }

        if (functionDefinition.getSymbolIsRegex()) {
          // TODO this is not in use atm. We need some way to define that there is a regex symbol...
          patternOperatorMap.put(Pattern.compile(functionDefinition.getSymbol()), functionDefinition.getFunction());
        }
        // TODO should we still add to the symbol operator map?
        symbolOperatorMap.put(functionDefinition.getSymbol(), functionDefinition.getFunction());
      }

      String className = functionDefinition.getMathMethod().getClassName();
      Map<String, Function> tmp = classMethodToFunction.getOrDefault(className, new HashMap<>());
      tmp.put(functionDefinition.getMathMethod().getMethodName(), functionDefinition.getFunction());
      classMethodToFunction.put(className, tmp);
    }
    patterns.add(NUM_REGEX);
    patterns.add(String.format("%s", SYMBOL_GROUPS));

    // TODO replace this with a proper factorial function definition
    symbolOperatorMap.put("!", Function.FACTORIAL);
    functionToDefinition.put(Function.FACTORIAL, FunctionDefinition.newBuilder().setIsInPlace(false).build());

    pattern = Pattern.compile(String.join("|", patterns));
  }

  public FunctionDefinition getFunctionDefinition(Function function) { return functionToDefinition.get(function); }

  public Function mapStringToFunction(String token) {
    token = token.toLowerCase();
    if (multiVariableFunctionMap.containsKey(token)) {
      return multiVariableFunctionMap.get(token);
    }
    if (symbolOperatorMap.containsKey(token)) {
      return symbolOperatorMap.get(token);
    }
    for (Map.Entry<Pattern, Function> entry : patternOperatorMap.entrySet()) {
      if (entry.getKey().matcher(token).find()) {
        return entry.getValue();
      }
    }
    throw new MathParseException(token, null, MathParseException.ParseExceptionType.FUNCTION_NOT_DEFINED);
  }

  public boolean isMultiVariableFunction(String symbol) {
    return multiVariableFunctionMap.containsKey(symbol);
  }

  public boolean isSymbolFunction(String symbol) {
    if (symbolOperatorMap.containsKey(symbol)) {
      return true;
    }
    for (Pattern key : patternOperatorMap.keySet()) {
      if (key.matcher(symbol).find()) {
        return true;
      }
    }
    return false;
  }

  public boolean isSymbolFunction(Function function) {
    return symbolOperatorMap.containsValue(function);
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
          eval.getClass().getName()
        ).get(
          method.getName()
        );

        if (function == null) {
          logger.warn(String.format(
            "Unable to map '%s' from class '%s'",
            method.getName(),
            eval.getClass().getName()
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
