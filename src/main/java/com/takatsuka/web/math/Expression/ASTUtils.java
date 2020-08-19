package com.takatsuka.web.math.Expression;

import com.takatsuka.web.rules.Function;
import com.takatsuka.web.rules.Operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ASTUtils {

    HashMap<String, Operator> operators;
    HashMap<String, Function> functions;

    ASTUtils(HashMap<String, Function> functions, HashMap<String, Operator> operators){
        this.functions = functions;
        this.operators = operators;
    }

    public void recurDescentToAST(ArrayList<Token> tokens){
        Stack<String> opStack = new Stack<>();
        Stack<ASTNode> valStack = new Stack<>();


    }
}
