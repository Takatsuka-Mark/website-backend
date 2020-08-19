package com.takatsuka.web.math.Expression;

enum OperatorType {
    ADD,
    SUB,
    MUL,
    DIV,
    POW,
    MOD,

    TOTIENT,
    SQRT;
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
