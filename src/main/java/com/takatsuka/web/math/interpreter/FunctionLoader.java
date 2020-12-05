package com.takatsuka.web.math.interpreter;

import com.takatsuka.web.interpreter.FunctionDefinition;
import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.utils.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class FunctionLoader {
  private static final Logger logger = MathLogger.forCallingClass();
  private static final Pattern filePattern = Pattern.compile(".*\\.json");

  FileUtils fileUtils;

  public FunctionLoader(FileUtils fileUtils) {
    this.fileUtils = fileUtils;
  }

  public List<FunctionDefinition> loadFunctions() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    Set<String> files =
        new Reflections("functions/basic", new ResourcesScanner()).getResources(filePattern);

    files.addAll(
        new Reflections("functions/exponential", new ResourcesScanner()).getResources(filePattern));

    files.addAll(
        new Reflections("functions/trig", new ResourcesScanner()).getResources(filePattern));

    logger.info("Found '{}' rules to load: {}", files.size(), Arrays.toString(files.toArray()));

    List<FunctionDefinition> loadedFunctions = new ArrayList<>();

    for (String file : files) {
      InputStream stream = classLoader.getResourceAsStream(file);
      loadedFunctions.add(fileUtils.readFunctionFromJson(stream));
    }

    return loadedFunctions;
  }
}
