package com.takatsuka.web.math.rules;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RuleLoader {



    public void loadRules(){
        String path = "./functions/totient.json";

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(path));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
