package com.takatsuka.web.utils.exceptions;

public class MathExecException extends MathException{
  private final String cause;
  private final String location;
  private final MathExecExceptionType mathExecExceptionType;

  public MathExecException(String cause, String location, MathExecExceptionType mathExecExceptionType) {
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
      case FUNCTION_IN_TESTING:
        typeMessage =
                String.format("Function '%s' is in testing and not available right now.", cause);
        break;
      case POSITIVE_REQUIRED:
        typeMessage =
                String.format("A positive value is required in the %s.", cause);
      default:
        typeMessage = "Unknown Execution Failure.";
        break;
    }

    return typeMessage;
  }

  public enum MathExecExceptionType {
    DIV_BY_ZERO,
    FUNCTION_IN_TESTING,
    POSITIVE_REQUIRED
  }
}
