package com.library.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NaverErrorDecoder implements ErrorDecoder {

  private final ObjectMapper objectMapper;

  public NaverErrorDecoder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  // 에러가 발생하면 decode에서 처리가 가능
  @Override
  public Exception decode(String s, Response response) {
    try {
      /**
       * 네이버 검색 에러 발생시 응답 값
       * {
       *   "errorMessage": "Incorrect query request (잘못된 쿼리요청입니다.)",
       *   "errorCode": "SE01"
       * }
       */
      String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
      NaverErrorResponse errorResponse = objectMapper.readValue(body, NaverErrorResponse.class);
      throw new RuntimeException(errorResponse.getErrorMessage());
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }

  }
}
