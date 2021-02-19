package com.takatsuka.web.utils.exceptions;

public class MathExecException extends RuntimeException implements MathException{
  private final String cause;
  private final int location;
  private final MathExecExceptionType mathExecExceptionType;

  public MathExecException(String cause, int location, MathExecExceptionType mathExecExceptionType) {
    this.cause = cause;
    this.location = location;
    this.mathExecExceptionType = mathExecExceptionType;
  }

  @Override
  public String toString() {
    String typeMessage;
    switch(mathExecExceptionType){
      case DIV_BY_ZERO:
        typeMessage = "Cannot divide by zero.";
        break;
      default:
        typeMessage = "Unknown Execution Failure.";
        break;
    }

    return "MathExecException: " + typeMessage;
  }

  public enum MathExecExceptionType {
    DIV_BY_ZERO,
  }
}
