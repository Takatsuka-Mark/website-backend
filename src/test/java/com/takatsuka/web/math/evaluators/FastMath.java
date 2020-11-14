package com.takatsuka.web.math.evaluators;

import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

public class FastMath {
  private final int NUM_RANDOM_TESTS = 10;
  private Random rand;

  FastMath fastMath;

  @Before
  public void init() {
    fastMath = new FastMath();
    rand = new Random();
  }

  @Test
  public void testRandomSmallInt() {
    for (int i = 0; i < NUM_RANDOM_TESTS; i++) {
      int rand1 = rand.nextInt();
      int rand2 = rand.nextInt();
//      Truth.assertThat(BigInteger.valueOf(rand1 * rand2))
//          .isEqualTo(fastMath.FFTMultiply(BigInteger.valueOf(rand1), BigInteger.valueOf(rand2)));
    }
  }
}
