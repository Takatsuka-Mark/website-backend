package com.takatsuka.web.math.interpreter;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MathParser {

  private Pattern regex = FunctionMapper.getPattern();

  public MathParser() {}

  public ArrayList<String> tokenize(String expression) {
    // TODO could look for only positives, and interpret '-' as * -1
    Matcher m = regex.matcher(expression);

    ArrayList<String> tokens = new ArrayList<>();
    while (m.find()) {
      String tok = m.group();
      tokens.add(tok);
    }

    return tokens;
  }

  //    private void tokenize(String expression) {
  //        final String numRegex = "(\\d*\\.\\d+)|(\\.\\d+)|(\\d+)";
  //        ArrayList<String> patterns = new ArrayList<>();
  //        patterns.add(numRegex); // double and integer
  //        patterns.add("[+\\-*/()^]"); // operators
  //        //    patterns.add("[()]");
  //        patterns.addAll(functionRegex); // functions
  //        patterns.addAll(operatorRegex);
  //
  //        tokens = new ArrayList<>();
  //
  //        Pattern reg = Pattern.compile(String.join("|", patterns));
  //        Matcher m = reg.matcher(expression);
  //
  //        while (m.find()) {
  //            String tok = m.group();
  //            if (Pattern.compile(numRegex).matcher(tok).groupCount() == 1) {
  //                tokens.add(new Token(tok, Token.tokenType.VALUE));
  //            } else {
  //                tokens.add(new Token(tok, Token.tokenType.OPERATOR));
  //            }
  //        }
  //
  //        logger.info(
  //                "Tokenized input to: '{}'",
  //                Arrays.toString(tokens.stream().map(token -> token.value).toArray()));
  //    }
}
