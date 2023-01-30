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
    patternOperatorMap = new HashMap<>();
    ArrayList<String> patterns = new ArrayList<>();

    patterns.add(FUNC_REGEX);
    patterns.add(",");

    // Build multi-variable functions
    for (FunctionDefinition functionDefinition : functionsList) {
      // Map the symbol to the function
      multiVariableFunctionMap.put(
          functionDefinition.getSymbol().toLowerCase(), functionDefinition.getFunction());

      // Map the function to max-args.
      if (functionDefinition.getIsInPlace()){
        functionToMaxArgMap.put(functionDefinition.getFunction(), 2);
      } else {
        functionToMaxArgMap.put(functionDefinition.getFunction(), functionDefinition.getMaxArgs());
      }



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
      } else {
        functionToParamPattern.put(
          functionDefinition.getFunction(), Pattern.compile(functionDefinition.getParamPattern())
        );
      }
  
      // functionToParamTypeMap.put(
      //     functionDefinition.getFunction(), functionDefinition.getMathMethod().getParamTypesList());
      String className = functionDefinition.getMathMethod().getClassName();
      Map<String, Function> tmp = classMethodToFunction.getOrDefault(className, new HashMap<String, Function>());
      tmp.put(functionDefinition.getMathMethod().getMethodName(), functionDefinition.getFunction());
      classMethodToFunction.put(className, tmp);
    }
    patterns.add(NUM_REGEX);
    patterns.add(String.format("%s", SYMBOL_GROUPS));

    // Build operator
    symbolOperatorMap.put("!", Function.FACTORIAL);
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
    for (Map.Entry<Pattern, Function> entry : patternOperatorMap.entrySet()) {
      if (entry.getKey().matcher(token).find()) {
        return entry.getValue();
      }
    }
    throw new MathParseException(token, null, MathParseException.ParseExceptionType.FUNCTION_NOT_DEFINED);
  }

  public int getMaxArgs(Function function) {
    // This should be deprecated. We can determine the number of args based on the regex, so this shouldn't be necessary.
    return functionToMaxArgMap.getOrDefault(function, 2);
  }

  public boolean isMultiVariableFunction(String symbol) {
    return multiVariableFunctionMap.containsKey(symbol);
  }

  public boolean isMultiVariableFunction(Function function) {
    return multiVariableFunctionMap.containsValue(function);
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

  public Pattern getNumPattern() {
    return NUM_PATTERN;
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
