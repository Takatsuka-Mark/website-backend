package com.takatsuka.web.utils;

import com.google.protobuf.util.JsonFormat;
import com.takatsuka.web.rules.Function;
import com.takatsuka.web.rules.Operator;
import com.takatsuka.web.rules.Rule;
import org.springframework.stereotype.Component;

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

    public Function readRuleFromJson(File file) {
        try {
            Function.Builder ruleBuilder = Function.newBuilder();
            JsonFormat.parser().merge(new FileReader(file), ruleBuilder);

            return ruleBuilder.build();
        } catch (IOException e) {  // TODO verify these are produced
            e.printStackTrace();
            throw new RuntimeException("Unable to parse file to JSON", e);
        }
    }

    // TODO generify this.
    public Operator readOperatorFromJson(File file) {
        try {
            Operator.Builder ruleBuilder = Operator.newBuilder();
            JsonFormat.parser().merge(new FileReader(file), ruleBuilder);

            return ruleBuilder.build();
        } catch (IOException e) {  // TODO verify these are produced
            e.printStackTrace();
            throw new RuntimeException("Unable to parse file to JSON", e);
        }
    }
}
