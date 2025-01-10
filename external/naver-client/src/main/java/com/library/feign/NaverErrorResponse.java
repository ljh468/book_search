package com.library.feign;

import lombok.Getter;

@Getter
public class NaverErrorResponse {

  private final String errorMessage;

  private final String errorCode;

  public NaverErrorResponse(String errorMessage, String errorCode) {
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }
}
