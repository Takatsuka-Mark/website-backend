package com.takatsuka.web.math.rules;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.rules.Function;
import com.takatsuka.web.rules.Operator;
import com.takatsuka.web.rules.Rule;
import com.takatsuka.web.utils.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class RuleLoader {
  private static final Logger logger = MathLogger.forCallingClass();

  FileUtils fileUtils;

  public RuleLoader(FileUtils fileUtils) {
    this.fileUtils = fileUtils;
  }

  public HashMap<String, Function> loadFunctions() {
//    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//
//    List<File> files =
//        new Reflections("rules.functions", new ResourcesScanner())
//            .getResources(Pattern.compile(".*\\.json")).stream()
//                .map(resource -> new File(classLoader.getResource(resource).getFile()))
//                .collect(Collectors.toList());
//
//    logger.info("Found '{}' rules to load: {}", files.size(), Arrays.toString(files.toArray()));
//
//    // map of the rule regex to the JSONObject.
//    HashMap<String, Function> loadedRules = new HashMap<>();
//
//    for (File file : files) {
//      Function rule = fileUtils.readRuleFromJson(file);
//      loadedRules.put(rule.getPattern(), rule);
//    }
//
//    return loadedRules;
    return null;
  }

  public HashMap<String, Operator> loadOperators() {
//    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//
//    List<File> files =
//            new Reflections("rules.operators", new ResourcesScanner())
//                    .getResources(Pattern.compile(".*\\.json")).stream()
//                    .map(resource -> new File(classLoader.getResource(resource).getFile()))
//                    .collect(Collectors.toList());
//
//    logger.info("Found '{}' operators to load: {}", files.size(), Arrays.toString(files.toArray()));
//
//    // map of the rule regex to the JSONObject.
//    HashMap<String, Operator> loadedRules = new HashMap<>();
//
//    for (File file : files) {
//      Operator rule = fileUtils.readOperatorFromJson(file);
//      loadedRules.put(rule.getPattern(), rule);
//    }
//
//    return loadedRules;
    return null;
  }
}
