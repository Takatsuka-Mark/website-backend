package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;

import java.util.List;

public class Evaluator {
  public static double evaluateFunction(Function function, List<String> args){
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
}
