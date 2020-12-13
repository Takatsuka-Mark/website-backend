package com.takatsuka.web.math.evaluators;

import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;

public class RandomEvaluatorTest {

  private RandomEvaluator randomEvaluator;
  private Random random;

  @Before
  public void init() {
    randomEvaluator = new RandomEvaluator(new MathContext(100));
    random = new Random();
  }

  @Test
  public void testRandomInt() {
    BigInteger min = new BigInteger(random.nextInt(64), random);
    BigInteger max =
        new BigInteger(random.nextInt(64), random)
            .add(min)
            .add(BigInteger.TEN); // At minimum this is 10 > min

    BigInteger result = new BigInteger(randomEvaluator.randomInt(min, max));

    Truth.assertThat(result).isAtLeast(min);
    Truth.assertThat(result).isLessThan(max);
  }
}
