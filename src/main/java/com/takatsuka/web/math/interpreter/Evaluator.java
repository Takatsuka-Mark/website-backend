package com.takatsuka.web.math.interpreter;

import com.google.common.math.BigIntegerMath;
import com.takatsuka.web.interpreter.Function;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.evaluators.BasicEvaluator;
import com.takatsuka.web.math.evaluators.ExponentialEvaluator;
import com.takatsuka.web.math.evaluators.EvaluatorGrouping;
import com.takatsuka.web.math.evaluators.RandomEvaluator;
import com.takatsuka.web.math.evaluators.TrigEvaluator;
import com.takatsuka.web.utils.exceptions.MathExecException;

import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Evaluator {
  private static final Logger logger = MathLogger.forCallingClass();

  public static final String DEFAULT = BigDecimal.ZERO.toString();
  private final MathContext mathContext;
  private final HashMap<Function, HashMap<String, Method>> newMethodMap; 
  private final List<EvaluatorGrouping> allEvaluators;

  public Evaluator(FunctionMapper functionMapper) {
    this(100, functionMapper);
  }

  public Evaluator(int precision, FunctionMapper functionMapper) {
    // TODO(mark): make this final once math_context is moved to method.
    allEvaluators = new ArrayList<>();
    mathContext = new MathContext(precision);
    // Could find these classes with a reflection https://www.baeldung.com/java-find-all-classes-in-package
    allEvaluators.add(new BasicEvaluator(mathContext));
    allEvaluators.add(new RandomEvaluator(mathContext));
    allEvaluators.add(new ExponentialEvaluator(mathContext));
    allEvaluators.add(new TrigEvaluator(mathContext));

    this.newMethodMap = functionMapper.mapEvalsToMethods(allEvaluators);
  }

  public String evaluateFunction(Function function, List<String> args) {
    logger.debug("Evaluating function '{}' with args '{}'", function, args.toString());

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
        BigDecimal param1 = new BigDecimal(args.get(0));
        BigDecimal param2 = new BigDecimal(args.get(1));
        if (param2.equals(BigDecimal.ZERO)) {
          throw new MathExecException(
              String.format("%s / %s", param1.toString(), param2.toString()),
              null,
              MathExecException.MathExecExceptionType.DIV_BY_ZERO);
        }
        return String.valueOf(param1.divide(param2, mathContext));
      case MOD:
        return String.valueOf(new BigInteger(args.get(0)).mod(new BigInteger(args.get(1))));
      case POWER:
        // TODO placeholder until these are converted to funcs.
        return ((ExponentialEvaluator)(allEvaluators.get(2))).pow(new BigDecimal(args.get(0)), new BigInteger(args.get(1)));
      case FACTORIAL:
        return String.valueOf(BigIntegerMath.factorial(Integer.parseInt(args.get(0))));
      // case INT_DIVIDE:
        // return String.valueOf(new BigInteger(args.get(0)).divide(new BigInteger(args.get(1))));
      default:
        break;
    }
    HashMap<String, Method> methodCandidates = this.newMethodMap.get(function);

    // TODO this is just for testing - select only one class
    Entry<String, Method> method = methodCandidates.entrySet().iterator().next();

    EvaluatorGrouping evalClass = (EvaluatorGrouping) this.allEvaluators.stream().filter(eval -> eval.getClass() == method.getValue().getDeclaringClass()).collect(Collectors.toList()).toArray()[0];

    return evaluateDynamicFunction(evalClass, method.getValue(), function, args, method.getKey());
  }

  private String evaluateDynamicFunction(
    Object executorClass, Method method, Function function, List<String> args, String paramTypes
  ) {
    List<Object> params = parseParams(args, paramTypes);
    try {
      return method.invoke(executorClass, params.toArray()).toString();
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException("Could not evaluate the function");
    }
  }

  private List<Object> parseParams(List<String> args, String paramTypes) {
    List<Object> finalArgs = new ArrayList<>();
    System.out.println(paramTypes);
    // TODO improve this
    if (paramTypes.equals("Z+")) {
      List<BigInteger> argsList = new ArrayList<>();
      for (String arg : args) {
        argsList.add(new BigInteger(arg));
      }
      finalArgs.add(argsList);
      return finalArgs;
    } else if (paramTypes.equals("R+")) {
      List<BigDecimal> argsList = new ArrayList<>();
      for (String arg : args) {
        argsList.add(new BigDecimal(arg));
      }
      finalArgs.add(argsList);
      return finalArgs;
    }


    Queue<String> argQueue = new LinkedList<String>(args);

    for (char argChar : paramTypes.toCharArray()) {
      String thisArg = argQueue.poll();
      switch(argChar) {
        case 'Z':
          finalArgs.add(new BigInteger(thisArg));
          break;
        case 'R':
          finalArgs.add(new BigDecimal(thisArg));
          break;
        case 'z':
          finalArgs.add(Integer.parseInt(thisArg));
          break;
        case 'r':
          finalArgs.add(Double.parseDouble(thisArg));
          break;
      }
    }

    if (!argQueue.isEmpty()) {
      // TODO fix message.
      throw new RuntimeException("There are an improper number of arguments");
    }

    return finalArgs;
  }
}
