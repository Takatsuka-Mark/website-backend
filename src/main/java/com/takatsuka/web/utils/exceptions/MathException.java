package com.takatsuka.web.utils.exceptions;

public abstract class MathException extends RuntimeException{
    public String toString() {
        return "There was an unknown error while executing this statement.";
    }
}
