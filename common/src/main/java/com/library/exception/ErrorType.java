package com.library.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

  UNKNOWN("알 수 없는 에러입니다."),

  EXTERNAL_API_ERROR("외부 API 호출 에러 입니다."),

  INVALID_PARAMETER("잘못된 요청 값 입니다."),

  NO_RESOURCE("존재하지 않는 리소스입니다.");

  private final String description;

  ErrorType(String description) {
    this.description = description;
  }
}
