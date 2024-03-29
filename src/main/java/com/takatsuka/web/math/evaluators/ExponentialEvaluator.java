package com.takatsuka.web.math.evaluators;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.utils.MathEvaluator;
import com.takatsuka.web.math.utils.MathMethod;
import com.takatsuka.web.utils.ThreadUtils;
import com.takatsuka.web.utils.exceptions.MathExecException;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Optional;

@MathEvaluator
public class ExponentialEvaluator implements EvaluatorGrouping {

  private static final Logger logger = MathLogger.forCallingClass();
  private static final BigDecimal TWO = new BigDecimal(BigInteger.TWO);
  private final MathContext mathContext;

  public ExponentialEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
  }

  @MathMethod("R")
  public String lg(BigDecimal input) {
    return logn(TWO, input);
  }

  @MathMethod("R")
  public String log(BigDecimal input) {
    return logn(BigDecimal.TEN, input);
  }

  @MathMethod("RR")
  public String logn(BigDecimal base, BigDecimal value) {
    throw new MathExecException(
        "logn", null, MathExecException.MathExecExceptionType.FUNCTION_IN_TESTING);
  }

  @MathMethod("RZ")
  public String pow(BigDecimal base, BigInteger exp) {
    return modPowHelper(base, exp, Optional.empty());
  }

  @MathMethod("RZZ")
  public String modPow(BigDecimal base, BigInteger exp, BigInteger mod) {
    if (mod.signum() > 0) {
      return modPowHelper(base, exp, Optional.of(mod));
    }
    throw new MathExecException(
        "modulus of modPow", null, MathExecException.MathExecExceptionType.POSITIVE_REQUIRED);
  }

  private String modPowHelper(BigDecimal base, BigInteger exp, Optional<BigInteger> optionalMod) {
    boolean ignoreMod = optionalMod.isEmpty();
    boolean isNegative = false;
    // DO MATH
    if (exp.signum() < 0) {
      isNegative = true;
      exp = exp.abs();
    }

    BigDecimal accumulator = BigDecimal.ONE;
    BigDecimal decimal_mod = BigDecimal.ZERO; // Temporary
    if (!ignoreMod) {
      decimal_mod = new BigDecimal(optionalMod.get());
    }

    while (exp.signum() > 0) {
      ThreadUtils.throwIfInterrupted(logger); // Catch for interrupted thread.

      if (exp.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
        accumulator = accumulator.multiply(base);
      }
      base = base.multiply(base);
      exp = exp.shiftRight(1);

      if (!ignoreMod) {
        accumulator = accumulator.remainder(decimal_mod);
        base = base.remainder(decimal_mod);
      }
    }

    String result = accumulator.toString();

    if (isNegative) {
      result = BigDecimal.ONE.divide(accumulator, mathContext).toString();
    }

    return result;
  }
}
