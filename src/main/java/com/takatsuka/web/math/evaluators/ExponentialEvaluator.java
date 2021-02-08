package com.takatsuka.web.math.evaluators;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.utils.ThreadUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class ExponentialEvaluator {

  private static final Logger logger = MathLogger.forCallingClass();
  private static final BigDecimal TWO = new BigDecimal(BigInteger.TWO);
  private final MathContext mathContext;

  public ExponentialEvaluator(MathContext mathContext) {
    this.mathContext = mathContext;
  }

  public String lg(BigDecimal input){
    return logn(TWO, input);
  }

  public String log(BigDecimal input) {
    return logn(BigDecimal.TEN, input);
  }

  public String logn(BigDecimal base, BigDecimal value) {
    return "Needs implementation.";
  }

  public String pow(BigInteger base, BigInteger exp) {
    return modPowHelper(base, exp, null, true);
  }

  public String modPow(BigInteger base, BigInteger exp, BigInteger mod) {
    if(mod.signum() > 0) {
      return modPowHelper(base, exp, mod, false);
    }
    return "0"; // TODO(mark): notify that the sign of the mod was incorrect.
  }

  private String modPowHelper(BigInteger base, BigInteger exp, BigInteger mod, boolean ignoreMod) {
    boolean isNegative = false;
    // DO MATH
    if (exp.signum() < 0) {
      isNegative = true;
      exp = exp.abs();
    }

    BigInteger accumulator = BigInteger.ONE;

    while (exp.signum() > 0) {
      ThreadUtils.throwIfInterrupted(logger); // Catch for interrupted thread.

      if (exp.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
        accumulator = accumulator.multiply(base);
      }
      base = base.multiply(base);
      exp = exp.shiftRight(1);

      if(!ignoreMod) {
        accumulator = accumulator.mod(mod);
        base = base.mod(mod);
      }
    }

    String result = accumulator.toString();

    if(isNegative) {
      result = BigDecimal.ONE.divide(new BigDecimal(accumulator), mathContext).toString();
    }

    return result;
  }
}
