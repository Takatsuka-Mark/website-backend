package com.takatsuka.web.Expression;

import com.takatsuka.web.MathOps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEval {
    ArrayList<Token> tokens;
    HashMap<String, Operator> operators;
    HashMap<String, Operator> functions;
    AST tree;
    int currToken;
    MathOps mathOps;

    public ExpressionEval(){
        this.tokens = new ArrayList<>();
        this.tree = new AST();
        this.currToken = 0;
        this.mathOps = new MathOps();

        this.functions = new HashMap<>();
        this.functions.put("totient", new Operator(false, OperatorType.TOTIENT, 100, "totient"));

        this.operators = new HashMap<>();
        this.operators.put("^", new Operator(true, OperatorType.POW, 4, "^"));
        this.operators.put("*", new Operator(false, OperatorType.MUL, 3, "*"));
        this.operators.put("/", new Operator(false, OperatorType.DIV, 3, "/"));
        this.operators.put("+", new Operator(false, OperatorType.ADD, 2, "+"));
        this.operators.put("-", new Operator(false, OperatorType.SUB, 2, "-"));
    }

    public String Evaluate(String expression){
        tokenize(expression);
        final ASTNode tree = parseToAST();
        return Double.toString(evaluateAST(tree));
//        return Arrays.toString(tokens.toArray());
    }

    private void tokenize(String expression){
        final String numRegex = "(\\d*\\.\\d+)|(\\.\\d+)|(\\d+)";
        ArrayList<String> patterns = new ArrayList<>();
        patterns.add(numRegex); // double and integer
        patterns.add("[+\\-*/()^]");        // operators
        patterns.add("totient");            // functions

        tokens = new ArrayList<>();

        Pattern reg = Pattern.compile(String.join("|", patterns));
        Matcher m = reg.matcher(expression);

        while(m.find()){
            String tok = m.group();
            if(Pattern.compile(numRegex).matcher(tok).groupCount() == 1){
                tokens.add(new Token(tok, Token.tokenType.VALUE));
            }
            else{
                tokens.add(new Token(tok, Token.tokenType.OPERATOR));
            }
        }
    }

    private void addNode(Stack<ASTNode> stack, String operator){
        final ASTNode right = stack.pop();
        final ASTNode left = stack.pop();
        stack.push(new OperatorNode(left, right, operator));
    }

    private void addFunctionNode(Stack<ASTNode> stack, String operator){
        stack.push(new OperatorNode(stack.pop(), null, operator));
    }

    private ASTNode parseToAST(){
        Stack<String> opStack = new Stack<>();
        Stack<ASTNode> valStack = new Stack<>();

        forLoop:
        for(Token token : tokens){
            String val = token.getValue();
            switch (val){
                case "(":
                    opStack.push(val);
                    break;

                case ")":
                    while(!opStack.isEmpty()){
                        val = opStack.pop();
                        if("(".equals(val)){
                            continue forLoop;
                        }
                        else{
                            addNode(valStack, val);
                        }
                    }
                    System.out.println("Unbalanced Parens");

                default:
                    if(operators.containsKey(val)) {
                        final Operator op1 = operators.get(val);
                        Operator op2;
                        while (!opStack.isEmpty() && null != (op2 = operators.get(opStack.peek()))) {
                            if ((!op1.isRightAsociative()
                                    && 0 == op1.comparePrecedence(op2)) ||
                                    op1.comparePrecedence(op2) < 0) {
                                opStack.pop();
                                addNode(valStack, op2.getSymbol());
                            } else {
                                break;
                            }
                        }
                        opStack.push(val);
                    }
                    else if (functions.containsKey(val)){
                        final Operator func = functions.get(val);
                        while(!opStack.isEmpty()) {
                            opStack.pop();
                            addFunctionNode(valStack, func.getSymbol());
                        }
                        opStack.push(val);
                    }
                    else{
                        valStack.push(new NumNode(val));
                    }
            }
        }
        while(!opStack.isEmpty()){
            String op = opStack.pop();
            if(operators.containsKey(op))
                addNode(valStack, op);
            else
                addFunctionNode(valStack, op);
        }

        return valStack.pop();
    }

    private double evaluateAST(ASTNode root){
        if(root.getNodeType() == NodeType.OPERATOR) {
            OperatorNode operator = (OperatorNode)root;
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
                case "totient":
                    return mathOps.totient((int)evaluateAST(operator.left));
                default:
                    return 0.0;
            }
        }
        else{
            NumNode num = (NumNode)root;
            return Double.parseDouble(num.value);
        }
    }
}
