package com.takatsuka.web.Expression;

public class NumNode extends ASTNode{

    public final String value;

    public NumNode(String value){
        super(NodeType.VALUE);
        this.value = value;
    }
}
