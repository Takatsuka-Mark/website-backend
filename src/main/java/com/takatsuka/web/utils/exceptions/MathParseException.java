package com.takatsuka.web.utils.exceptions;

public class MathParseException extends MathException {
  private final String cause;
  private final String location;
  private final ParseExceptionType parseExceptionType;

  public MathParseException(String cause, String location, ParseExceptionType exceptionType) {
    this.cause = cause;
    this.location = location;
    this.parseExceptionType = exceptionType;
  }

  @Override
  public String toString() {
    String typeMessage;
    switch (parseExceptionType) {
      case FUNCTION_NOT_DEFINED:
        typeMessage = String.format("Function '%s' is not defined.", cause);
        break;
      default:
        typeMessage = "Unknown.";
        break;
    }

    return typeMessage;
  }

  public enum ParseExceptionType {
    FUNCTION_NOT_DEFINED
  }
}
