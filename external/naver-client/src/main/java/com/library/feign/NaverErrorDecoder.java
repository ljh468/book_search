package com.library.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.ApiException;
import com.library.ErrorType;
import com.library.NaverErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class NaverErrorDecoder implements ErrorDecoder {

  private final ObjectMapper objectMapper;

  public NaverErrorDecoder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  // 에러가 발생하면 decode에서 처리가 가능
  @Override
  public Exception decode(String methodKey, // 실행하다가 실패한 메서드명
                          Response response) {
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
      throw new ApiException(errorResponse.getErrorMessage(),
                             ErrorType.EXTERNAL_API_ERROR,
                             HttpStatus.valueOf(response.status()));
    } catch (IOException ioException) {
      log.error("[NAVER] message parsing error. code={}, request={}, methodKey={}, errorMessage={}",
                response.status(), response.request(), methodKey, ioException.getMessage());

      throw new ApiException("naver message parsing error.",
                             ErrorType.EXTERNAL_API_ERROR,
                             HttpStatus.valueOf(response.status()));
    }

  }
}
