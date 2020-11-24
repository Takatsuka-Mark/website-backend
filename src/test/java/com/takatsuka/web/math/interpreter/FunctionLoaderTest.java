package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.math.evaluators.MathOps;
import com.takatsuka.web.math.interpreter.FunctionLoader;
import com.takatsuka.web.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;

public class FunctionLoaderTest {

  private MathOps mathOps;
  private FunctionLoader functionLoader;
  @Mock private FileUtils fileUtils;

  @BeforeEach
  void setup() {
    mathOps = new MathOps();  // TODO(mark): THIS
  }

  @Test
  void exampleTest() {
    assertThat(5).isEqualTo(5);
  }
}
