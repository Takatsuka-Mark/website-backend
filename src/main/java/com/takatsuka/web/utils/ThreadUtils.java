package com.takatsuka.web.utils;

import org.slf4j.Logger;

import java.util.concurrent.TimeoutException;

public class ThreadUtils {

  public static void throwIfInterrupted(Logger logger) {
    if (Thread.currentThread().isInterrupted()) {
      // Thread was interrupted. Throw
      logger.error(
          String.format(
              "Execution timed out when during call to: %s.%s",
              Thread.currentThread().getStackTrace()[2].getClassName(),
              Thread.currentThread().getStackTrace()[2].getMethodName()));
      throw new RuntimeException("Execution timed out.", new TimeoutException());
    }
  }
}
