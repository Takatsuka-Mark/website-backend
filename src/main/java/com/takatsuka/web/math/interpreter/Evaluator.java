package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.interpreter.ParamType;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.evaluators.BasicEvaluator;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Evaluator {
  private static final Logger logger = MathLogger.forCallingClass();

  private static final String DEFAULT = BigDecimal.ZERO.toString();
  private final MathContext mathContext;
  private final BasicEvaluator basicEvaluator;
  private final Map<Function, Method> methodMap;
  private final FunctionMapper functionMapper;

  public Evaluator(FunctionMapper functionMapper) {
    this(100, functionMapper);
  }

  public Evaluator(int precision, FunctionMapper functionMapper) {
    mathContext = new MathContext(precision);
    basicEvaluator = new BasicEvaluator(mathContext, DEFAULT);
    this.functionMapper = functionMapper;
    this.methodMap = functionMapper.getFunctionToMethodMap();
  }

  public String evaluateFunction(Function function, List<String> args) {
    switch (function) {
      case UNKNOWN_FUNCTION:
        // The function is unknown, just return the args if available.
        return args.get(0);

        // Symbol Functions (Excluding FAC)
      case ADD:
        return String.valueOf(
            new BigDecimal(args.get(0)).add(new BigDecimal(args.get(1)), mathContext));
      case SUBTRACT:
        return String.valueOf(
            new BigDecimal(args.get(0)).subtract(new BigDecimal(args.get(1)), mathContext));
      case MULTIPLY:
        return String.valueOf(
            new BigDecimal(args.get(0)).multiply(new BigDecimal(args.get(1)), mathContext));
      case DIVIDE:
        return String.valueOf(
            new BigDecimal(args.get(0)).divide(new BigDecimal(args.get(1)), mathContext));
      case MOD:
        return String.valueOf(new BigInteger(args.get(0)).mod(new BigInteger(args.get(1))));
    }

    // It is not a basic operation.
    Method method = methodMap.get(function);
    if (method.getDeclaringClass().equals(BasicEvaluator.class)) {
      List<Object> params = parseParams(args, functionMapper.getParamTypeList(function));
      try {
        return method.invoke(basicEvaluator, params.toArray()).toString();
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    return DEFAULT;
  }

  private List<Object> parseParams(List<String> args, List<ParamType> paramTypes) {
    List<Object> finalArgs = new ArrayList<>();

    // Cover the n param functions.
    if(paramTypes.get(0) == ParamType.DECIMAL_LIST){
      List<BigDecimal> argsList = new ArrayList<>();
      for (String arg : args) {
          argsList.add(new BigDecimal(arg));
      }
      finalArgs.add(argsList);
      return finalArgs;
    } else if(paramTypes.get(0) == ParamType.INTEGER_LIST) {
      List<BigInteger> argsList = new ArrayList<>();
      for (String arg : args) {
        argsList.add(new BigInteger(arg));
      }
      finalArgs.add(argsList);
      return finalArgs;
    }

    // Cover functions with a discrete number of parameters.
    for (int i = 0; i < args.size(); i++) {
      ParamType paramType;
      if (i < paramTypes.size()) {
        paramType = paramTypes.get(i);
      } else {
        paramType = paramTypes.get(paramTypes.size() - 1);
      }

      switch (paramType) {
        case DECIMAL:
          finalArgs.add(new BigDecimal(args.get(i)));
          break;
        case INTEGER:
          finalArgs.add(new BigInteger(args.get(i)));
          break;
      }
    }

    return finalArgs;
  }
}
