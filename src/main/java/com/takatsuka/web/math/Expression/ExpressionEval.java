package com.takatsuka.web.math.Expression;

import com.takatsuka.web.logging.MathLogger;
import com.takatsuka.web.math.evaluators.Constants;
import com.takatsuka.web.math.evaluators.MathOps;
import com.takatsuka.web.rules.Function;
import com.takatsuka.web.rules.Math_Method;
import com.takatsuka.web.rules.Operator;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExpressionEval {
  private static final Logger logger = MathLogger.forCallingClass();

  ArrayList<Token> tokens;
  HashMap<String, Operator> operators;
  HashMap<String, Function> functions;
  HashSet<String> functionRegex;
  HashSet<String> operatorRegex;
  AST tree;
  int currToken;

  MathOps mathOps;
  Constants constants;

  public ExpressionEval(HashMap<String, Function> functions, HashMap<String, Operator> operators) {
    this.tokens = new ArrayList<>();
    this.tree = new AST();
    this.currToken = 0;
    this.mathOps = new MathOps();
    this.constants = new Constants();

    this.functions = functions;
    this.functionRegex = new HashSet<>();
    this.functionRegex =
        (HashSet<String>)
            functions.values().stream().map(Function::getPattern).collect(Collectors.toSet());

    // Can remove this.
    this.operators = operators;
    this.operatorRegex =
        (HashSet<String>)
            operators.values().stream().map(Operator::getPattern).collect(Collectors.toSet());

    this.operators.put(
        "^",
        Operator.newBuilder()
            .setTitle("^")
            .setPattern("^")
            .setIsRightAssociative(true)
            .setPrecedence(4)
            .build());
    this.operators.put(
        "*",
        Operator.newBuilder()
            .setTitle("*")
            .setPattern("\\*")
            .setIsRightAssociative(false)
            .setPrecedence(3)
            .build());
    this.operators.put(
        "/",
        Operator.newBuilder()
            .setTitle("/")
            .setPattern("/")
            .setIsRightAssociative(false)
            .setPrecedence(3)
            .build());
    this.operators.put(
        "+",
        Operator.newBuilder()
            .setTitle("+")
            .setPattern("\\+")
            .setIsRightAssociative(false)
            .setPrecedence(2)
            .build());
    this.operators.put(
        "-",
        Operator.newBuilder()
            .setTitle("-")
            .setPattern("\\-")
            .setIsRightAssociative(false)
            .setPrecedence(2)
            .build());
  }

  public String Evaluate(String expression) {
    tokenize(expression);
    final ASTNode tree = parseToAST();
    return Double.toString(evaluateAST(tree));
  }

  private void tokenize(String expression) {
    final String numRegex = "(\\d*\\.\\d+)|(\\.\\d+)|(\\d+)";
    ArrayList<String> patterns = new ArrayList<>();
    patterns.add(numRegex); // double and integer
    patterns.add("[+\\-*/()^]"); // operators
    //    patterns.add("[()]");
    patterns.addAll(functionRegex); // functions
    patterns.addAll(operatorRegex);

    tokens = new ArrayList<>();

    Pattern reg = Pattern.compile(String.join("|", patterns));
    Matcher m = reg.matcher(expression);

    while (m.find()) {
      String tok = m.group();
      if (Pattern.compile(numRegex).matcher(tok).groupCount() == 1) {
        tokens.add(new Token(tok, Token.tokenType.VALUE));
      } else {
        tokens.add(new Token(tok, Token.tokenType.OPERATOR));
      }
    }

    logger.info(
        "Tokenized input to: '{}'",
        Arrays.toString(tokens.stream().map(token -> token.value).toArray()));
  }

  private void addNode(Stack<ASTNode> stack, String operator) {
    final ASTNode right = stack.pop();
    final ASTNode left = stack.pop();
    stack.push(new OperatorNode(left, right, operator));
  }

  private void addFunctionNode(Stack<ASTNode> stack, String operator) {
    stack.push(new OperatorNode(stack.pop(), null, operator));
  }

  private ASTNode parseToAST() {
    Stack<String> opStack = new Stack<>();
    Stack<ASTNode> valStack = new Stack<>();

    forLoop:
    for (Token token : tokens) {
      String val = token.getValue();
      switch (val) {
        case "(":
          opStack.push(val);
          break;
        case ")":
          while (!opStack.isEmpty()) {
            val = opStack.pop();
            if ("(".equals(val)) {
              continue forLoop;
            } else {
              addNode(valStack, val);
            }
          }
          System.out.println("Unbalanced Parens");

        default:
          // Found an operator instance
          if (operators.containsKey(val)) {
            final Operator op1 = operators.get(val);
            Operator op2;
            while (!opStack.isEmpty() && null != (op2 = operators.get(opStack.peek()))) {
              if ((!op1.getIsRightAssociative() && 0 == comparePrecedence(op1, op2))
                  || comparePrecedence(op1, op2) < 0) {
                opStack.pop();
                addNode(valStack, op2.getTitle()); // TODO should store the symbol
              } else {
                break;
              }
            }
            opStack.push(val);
          }
          // Found a function instance
          else if (functions.containsKey(val)) {
            final Function func = functions.get(val);
            while (!opStack.isEmpty()) {
              opStack.pop();
              addFunctionNode(valStack, func.getPattern()); // TODO should store the symbol
            }
            opStack.push(val);
          } else {
            valStack.push(new NumNode(val));
          }
      }
    }
    while (!opStack.isEmpty()) {
      String op = opStack.pop();
      if (operators.containsKey(op)) {
        addNode(valStack, op);
      } else {
        addFunctionNode(valStack, op);
      }
    }

    return valStack.pop();
  }

  private double evaluateAST(ASTNode root) {
    if (root.getNodeType() == NodeType.OPERATOR) {
      OperatorNode operator = (OperatorNode) root;
      logger.info("Evaluating on operator: '{}'", operator.operator);

      switch (operator.operator) {
        case "*":
          return evaluateAST(operator.left) * evaluateAST(operator.right);
        case "/":
          return evaluateAST(operator.left) / evaluateAST(operator.right);
        case "+":
          return evaluateAST(operator.left) + evaluateAST(operator.right);
        case "-":
          return evaluateAST(operator.left) - evaluateAST(operator.right);
        case "^":
          return Math.pow(evaluateAST(operator.left), evaluateAST(operator.right));
          //        case "mod":
          //          return mathOps.mod((int) evaluateAST(operator.left), (int)
          // evaluateAST(operator.right));
        default:
          if (functions.containsKey(operator.operator)) {
            return runFunction(operator);
          } else if (operators.containsKey(operator.operator)) {
            return runOperator(operator);
          }

          // final default.
          return 0.0;
      }
    } else {
      NumNode num = (NumNode) root;
      return Double.parseDouble(num.value);
    }
  }

  private double runFunction(OperatorNode operator) {
    Function rule = functions.get(operator.operator);
    Math_Method math_method = rule.getMathMethod();
    try {
      // TODO we shouldn't get this every time we run a function, to decrease latency.
      //  IE: Make a hashTable when we load the rules and access later
      Class<?> param0 = Class.forName(math_method.getParams(0));
      if (math_method.getClassName().endsWith("MathOps")) {
        Method method = mathOps.getClass().getMethod(math_method.getMethodName(), param0);
        return (double) method.invoke(mathOps, evaluateAST(operator.left));
      } else if (math_method.getClassName().endsWith("Constants")) {
        Method method = constants.getClass().getMethod(math_method.getMethodName(), param0);
        return (double) method.invoke(constants, evaluateAST(operator.left));
      }
    } catch (IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException
        | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return 0.0;
  }

  private double runOperator(OperatorNode operator) {
    Operator rule = operators.get(operator.operator);
    Math_Method math_method = rule.getMathMethod();
    try {
      // TODO we shouldn't get this every time we run a function, to decrease latency.
      //  IE: Make a hashTable when we load the rules and access later
      Class<?> param0 = Class.forName(math_method.getParams(0));
      Class<?> param1 = Class.forName(math_method.getParams(0));
      Method method = mathOps.getClass().getMethod(math_method.getMethodName(), param0, param1);
      return (double)
          method.invoke(mathOps, evaluateAST(operator.left), evaluateAST(operator.right));
    } catch (IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException
        | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return 0.0;
  }

  public int comparePrecedence(Operator op1, Operator op2) {
    return Integer.compare(op1.getPrecedence(), op2.getPrecedence());
  }
}
