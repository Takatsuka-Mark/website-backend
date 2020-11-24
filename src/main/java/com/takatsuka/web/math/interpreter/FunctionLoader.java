package com.takatsuka.web.math.interpreter;

import com.google.protobuf.util.JsonFormat;
import com.takatsuka.web.interpreter.FunctionDefinition;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.utils.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FunctionLoader {
  private static final Logger logger = MathLogger.forCallingClass();

  FileUtils fileUtils;

  public FunctionLoader(FileUtils fileUtils) {
    this.fileUtils = fileUtils;
  }

  public List<FunctionDefinition> loadFunctions() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    List<File> files =
        new Reflections("rules.functions", new ResourcesScanner())
            .getResources(Pattern.compile(".*\\.json")).stream()
                .map(resource -> new File(classLoader.getResource(resource).getFile()))
                .collect(Collectors.toList());

    logger.info("Found '{}' rules to load: {}", files.size(), Arrays.toString(files.toArray()));

    List<FunctionDefinition> loadedFunctions = new ArrayList<>();

    for (File file : files) {
      loadedFunctions.add(fileUtils.readFunctionFromJson(file));
    }

    return loadedFunctions;
  }
}
