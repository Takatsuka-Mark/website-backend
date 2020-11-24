package com.takatsuka.web.utils;

import com.google.protobuf.util.JsonFormat;
import com.takatsuka.web.interpreter.FunctionDefinition;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class FileUtils {

  public List<File> getAllFilenames(String folderPath, String filePattern) {
    File dir = new File(folderPath);
    File[] files = dir.listFiles((dir1, name) -> name.matches(filePattern));

    ArrayList<File> matchingFiles = new ArrayList<>();
    if (files != null) {
      Collections.addAll(matchingFiles, files);
    }

    return matchingFiles;
  }

  public FunctionDefinition readFunctionFromJson(File file) {
    try {
      FunctionDefinition.Builder functionBuilder = FunctionDefinition.newBuilder();
      JsonFormat.parser().merge(new BufferedReader(new FileReader(file)), functionBuilder);

      return functionBuilder.build();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(
          String.format("Unable to parse file '%s' to JSON", file.getName()), e);
    }
  }
}
