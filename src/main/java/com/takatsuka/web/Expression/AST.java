package com.takatsuka.web.Expression;

import java.util.Queue;

public class AST{
    private ASTNode root;

    public ASTNode getRoot() {
        return root;
    }

    public void setRoot(ASTNode root) {
        this.root = root;
    }



    public Queue<ASTNode> postOrder(){
        return null;
    }
}
