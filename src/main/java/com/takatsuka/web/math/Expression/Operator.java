package com.takatsuka.web.math.Expression;

public class Operator {
    private final boolean isRightAsociative;
    private final OperatorType type;
    private final int precedence;
    private final String symbol;

    public Operator(boolean isRightAsociative, OperatorType type, int precedence, String symbol) {
        this.isRightAsociative = isRightAsociative;
        this.type = type;
        this.precedence = precedence;
        this.symbol = symbol;
    }

    public boolean isRightAsociative() {
        return isRightAsociative;
    }

    public OperatorType getType() {
        return type;
    }

    public int getPrecedence() {
        return precedence;
    }

    public String getSymbol(){
        return symbol;
    }

    public int comparePrecedence(Operator op){
        return Integer.compare(this.precedence, op.getPrecedence());
    }
}
