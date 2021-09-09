package com.takatsuka.web.utils;

public class NumberUtils {
  public static int clamp(int min, int max, int val) {
    return Math.min(max, Math.max(min, val));
  }
}
