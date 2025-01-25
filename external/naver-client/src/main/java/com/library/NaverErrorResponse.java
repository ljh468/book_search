package com.library;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jackson 역직렬화는 기본생성자가 필요 - PROTECTED
public class NaverErrorResponse {

  private String errorMessage;

  private String errorCode;

  public NaverErrorResponse(String errorMessage, String errorCode) {
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }
}
