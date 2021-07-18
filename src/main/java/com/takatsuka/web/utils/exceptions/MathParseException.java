package com.takatsuka.web.utils.exceptions;

public class MathParseException extends MathException{
  private final String cause;
  private final int location;
  private final ParseExceptionType parseExceptionType;

  private MathParseException(String cause, int location, ParseExceptionType exceptionType) {
    this.cause = cause;
    this.location = location;
    this.parseExceptionType = exceptionType;
  }

  @Override
  public String toString() {
    String typeMessage;
    switch (parseExceptionType){
      case FUNCTION_NOT_DEFINED:
        typeMessage = "Function Not Defined.";
        break;
      case FUNCTION_IN_TESTING:
        typeMessage = "Function In Testing.";
        break;
      default:
        typeMessage = "Unknown.";
        break;
    }

    return String.format("MathParseException: Caused by: '%s' At: '%d'. %s", cause, location, typeMessage);
  }

  enum ParseExceptionType {
    FUNCTION_NOT_DEFINED,
    FUNCTION_IN_TESTING
  }
}
