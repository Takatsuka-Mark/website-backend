package com.takatsuka.web.utils;

import com.google.protobuf.util.JsonFormat;
import com.takatsuka.web.interpreter.FunctionDefinition;
import com.takatsuka.web.logging.MathLogger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FileUtils {

  private static final Logger logger = MathLogger.forCallingClass();
  private static final Pattern filePattern = Pattern.compile(".*\\.json");
  private static final List<String> FUNCTION_PATHS = getFilePaths();

  public static List<FunctionDefinition> loadFunctionsFromFiles() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    Set<String> files = new HashSet<>();
    for (String path : FUNCTION_PATHS) {
      files.addAll(new Reflections(path, new ResourcesScanner()).getResources(filePattern));
    }

    logger.info("Found '{}' rules to load: {}", files.size(), Arrays.toString(files.toArray()));

    List<FunctionDefinition> loadedFunctions = new ArrayList<>();

    for (String file : files) {
      InputStream stream = classLoader.getResourceAsStream(file);
      loadedFunctions.add(FileUtils.readFunctionFromJson(stream));
    }

    return loadedFunctions;
  }

  private static FunctionDefinition readFunctionFromJson(InputStream file) {
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

  private static List<String> getFilePaths() {
    return List.of("functions/basic", "functions/exponential", "functions/trig");
  }
}
