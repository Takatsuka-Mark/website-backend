package com.takatsuka.web.Expression;

enum OperatorType {
    ADD,
    SUB,
    MUL,
    DIV,
    POW,

    TOTIENT
}

enum NodeType {
    EXPRESSION,
    OPERATOR,
    VALUE,
    FUNCTION
}

public abstract class ASTNode {
    private final NodeType nodeType;

    public ASTNode(NodeType type){
        this.nodeType = type;
    }

    public NodeType getNodeType() {return nodeType;}
}
