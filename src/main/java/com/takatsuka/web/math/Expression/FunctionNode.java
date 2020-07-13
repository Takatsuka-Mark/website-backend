package com.takatsuka.web.math.Expression;

public class FunctionNode extends ASTNode{

    String function;
    ASTNode right;

    public FunctionNode(ASTNode right, String function) {
        super(NodeType.FUNCTION);
        this.function = function;
        this.right = right;
    }
}