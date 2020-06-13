package com.takatsuka.web.Expression;

public class OperatorNode extends ASTNode{

    public final ASTNode left;
    public final ASTNode right;
    public final String operator;


    public OperatorNode(ASTNode left, ASTNode right, String operator){
        super(NodeType.OPERATOR);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
}
