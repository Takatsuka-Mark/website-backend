package com.takatsuka.web.math;

import com.takatsuka.web.utils.NumberUtils;

import java.math.MathContext;

public class MathSettings {
  private final int MAX_PRECISION = 100;
  private final int MIN_PRECISION = 1;

  private final int dispPrecision;
  private final int compPrecision;

  public MathSettings(int dispPrecision, int compPrecision) {
    this.dispPrecision = NumberUtils.clamp(MIN_PRECISION, MAX_PRECISION, dispPrecision);
    this.compPrecision = NumberUtils.clamp(MIN_PRECISION, MAX_PRECISION, compPrecision);
  }

  public int getCompPrecision() {
    return compPrecision;
  }

  public int getDispPrecision() {
    return dispPrecision;
  }

  public MathContext getCompMathContext() {
    return new MathContext(compPrecision);
  }

  public MathContext getDispMathContext() {
    return new MathContext(dispPrecision);
  }
}
