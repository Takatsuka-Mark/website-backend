package com.takatsuka.web.utils;

import com.google.protobuf.util.JsonFormat;
import com.takatsuka.web.interpreter.FunctionDefinition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class FileUtils {

  public FunctionDefinition readFunctionFromJson(InputStream file) {
    try {
      FunctionDefinition.Builder functionBuilder = FunctionDefinition.newBuilder();
      JsonFormat.parser()
          .merge(new InputStreamReader(file, StandardCharsets.UTF_8), functionBuilder);

      return functionBuilder.build();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(
          String.format("Unable to parse file '%s' to JSON", file), e);
    }
  }
}
