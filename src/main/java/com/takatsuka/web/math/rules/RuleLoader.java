package com.takatsuka.web.math.rules;

import com.takatsuka.web.utils.FileUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class RuleLoader {

    FileUtils fileUtils;

    public RuleLoader(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @PostConstruct
    public HashMap<String, JSONObject> loadRules(String folderPath, String pattern) {
        // loading functions
        List<File> files = fileUtils.getAllFilenames("./functions", "*.//json");
        HashMap<String, JSONObject> loadedRules = new HashMap<>();

        for (File file : files) {
            JSONObject object = fileUtils.readToJSON(file);
            loadedRules.put(object.get("name").toString(), object);
        }

        return loadedRules;
    }
}
