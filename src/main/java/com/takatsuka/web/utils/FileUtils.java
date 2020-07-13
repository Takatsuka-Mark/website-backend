package com.takatsuka.web.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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

    public JSONObject readToJSON(File file) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));

            return (JSONObject) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
