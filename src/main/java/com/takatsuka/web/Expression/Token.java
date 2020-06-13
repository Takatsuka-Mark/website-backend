package com.takatsuka.web.Expression;

public class Token {
    public String value;
    public tokenType type;
    public enum tokenType{
        OPERATOR,
        VALUE,
        FUNCTION
    }

    public Token(String value, tokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public tokenType getType() {
        return type;
    }
}
