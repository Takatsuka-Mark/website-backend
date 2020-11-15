package com.takatsuka.web.math.interpreter;

import com.google.common.truth.Truth;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.takatsuka.web.interpreter.Function;
import org.junit.Test;

public class FunctionMapperTest {

  @Test
  public void testMapStringToFunction_symbolFunction() {
    String add = "+";
    Truth.assertThat(FunctionMapper.mapStringToFunction(add)).isEqualTo(Function.ADD);
  }
}
