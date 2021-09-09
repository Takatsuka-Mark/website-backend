package com.takatsuka.web.math;

import com.takatsuka.web.utils.NumberUtils;

import java.math.MathContext;
import java.util.Optional;

public class MathSettings {
  private final int MAX_PRECISION = 100;
  private final int MIN_PRECISION = 1;
  private final int DEFAULT_DISP_PRECISION = 10;
  private final int DEFAULT_COMP_PRECISION = 10;

  private final int dispPrecision;
  private final int compPrecision;

  public MathSettings() {
    this.dispPrecision = DEFAULT_DISP_PRECISION;
    this.compPrecision = DEFAULT_COMP_PRECISION;
  }

  public MathSettings(Optional<Integer> dispPrecision, Optional<Integer> compPrecision) {
    this.dispPrecision =
        dispPrecision
            .map(integer -> NumberUtils.clamp(MIN_PRECISION, MAX_PRECISION, integer))
            .orElse(DEFAULT_DISP_PRECISION);
    this.compPrecision =
        compPrecision
            .map(integer -> NumberUtils.clamp(MIN_PRECISION, MAX_PRECISION, integer))
            .orElse(DEFAULT_COMP_PRECISION);
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
