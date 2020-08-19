package com.takatsuka.web.math.Expression;

public class FunctionNode extends ASTNode{
    public final ASTNode value;
    public final String function;

    public FunctionNode(ASTNode value, String function){
        super(NodeType.FUNCTION);
        this.value = value;
        this.function = function;
    }
}
