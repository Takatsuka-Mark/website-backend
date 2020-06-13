package com.takatsuka.web.Expression;

public class ASTNodeSimp {
    public final ASTNodeSimp left;
    public final ASTNodeSimp right;
    public final String op;


    public ASTNodeSimp(ASTNodeSimp left, ASTNodeSimp right, String op){
        this.left = left;
        this.right = right;
        this.op = op;
    }
}
