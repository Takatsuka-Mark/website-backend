package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.Function;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Evaluator {

  private static final String DEFAULT = BigDecimal.ZERO.toString();
  private final MathContext mathContext;

  public Evaluator() {
    this(100);
  }

  public Evaluator(int precision) {
    mathContext = new MathContext(precision);
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

        // Mono Variable Functions (Excluding FAC)
      case ABSOLUTE_VALUE:
        return String.valueOf(new BigDecimal(args.get(0)).abs(mathContext));
      case SQUARE_ROOT:
        return String.valueOf(new BigDecimal(args.get(0)).sqrt(mathContext));

        // N Variable Functions
      case MAX:
        Optional<BigDecimal> result = args.stream().map(BigDecimal::new).max(BigDecimal::compareTo);
        if (result.isPresent()) {
          return result.get().toString();
        } else {
          return DEFAULT;
        }
    }
    return DEFAULT;
  }
}
