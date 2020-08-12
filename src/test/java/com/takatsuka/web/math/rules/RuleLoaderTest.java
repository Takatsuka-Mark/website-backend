package com.takatsuka.web.math.rules;

import com.takatsuka.web.math.evaluators.MathOps;
import com.takatsuka.web.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;

public class RuleLoaderTest {

  private MathOps mathOps;
  private RuleLoader ruleLoader;
  @Mock private FileUtils fileUtils;

  @BeforeEach
  void setup() {
    mathOps = new MathOps();
  }

  @Test
  void exampleTest() {
    assertThat(5).isEqualTo(5);
  }
}
