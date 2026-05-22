package com.wallet.demo.shared.domain.enums;

public enum MoneyCurrency {
  BRL;

  public static boolean isValid(String name) {
    if (name == null)
      return false;
    try {
      MoneyCurrency.valueOf(name);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

}
