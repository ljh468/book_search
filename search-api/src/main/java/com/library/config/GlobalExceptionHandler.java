package com.library.config;

import com.library.exception.ApiException;
import com.library.controller.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

import static com.library.exception.ErrorType.*;
import static java.util.Objects.nonNull;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorResponse> handleApiException(ApiException apiException) {
    log.error("Api Exception occurred. message={}, className={}",
              apiException.getErrorMessage(), apiException.getClass().getName());

    // ApiException의 HttpStatus는 중복으로 내려줄 필요가 없기 때문에 ErrorResponse 클래스를 정의해서 내려줄 것임
    return ResponseEntity.status(apiException.getHttpStatus())
                         .body(new ErrorResponse(apiException.getErrorMessage(),
                                                 apiException.getErrorType()));

  }

  // @Valid에서 발생하는 에러를 하기 위해서는 BindException 핸들링 해줘야 함
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleException(BindException bindException) {
    log.error("Bind Exception occurred. message={}, className={}",
              bindException.getMessage(), bindException.getClass().getName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorResponse(createMessage(bindException), INVALID_PARAMETER));
  }

  // URL 경로가 적절하지 않을 때 (없는 URL)
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException noResourceFoundException) {
    log.error("NoResourceFound Exception occurred. message={}, className={}",
              noResourceFoundException.getMessage(), noResourceFoundException.getClass().getName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorResponse(NO_RESOURCE.getDescription(), NO_RESOURCE));
  }

  // 파라미터가 비어있을 때
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException servletRequestParameterException) {
    log.error("MissingServletRequestParameter Exception occurred. parameterName={}, message={}, className={}",
              servletRequestParameterException.getParameterName(),
              servletRequestParameterException.getMessage(),
              servletRequestParameterException.getClass().getName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorResponse(INVALID_PARAMETER.getDescription(), INVALID_PARAMETER));
  }

  // 파라미터가 적절하지 않을 때
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
    log.error("MethodArgumentTypeMismatch Exception occurred. message={}, className={}",
              methodArgumentTypeMismatchException.getMessage(),
              methodArgumentTypeMismatchException.getClass().getName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorResponse(INVALID_PARAMETER.getDescription(), INVALID_PARAMETER));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {
    log.error("Exception occurred. message={}, className={}", exception.getMessage(), exception.getClass().getName());
    // ApiException의 HttpStatus는 중복으로 내려줄 필요가 없음
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(new ErrorResponse(UNKNOWN.getDescription(), UNKNOWN));

  }

  private String createMessage(BindException bindException) {
    // 디폴트 메시지가 있으면 그대로 내려주고,
    if (nonNull(bindException.getFieldError()) && nonNull(bindException.getFieldError().getDefaultMessage())) {
      return bindException.getFieldError().getDefaultMessage(); // .getFieldError는 Nullable
    }

    // 디폴트 메시지가 없으면 만들어야 함
    return bindException.getFieldErrors().stream()
                        .map(FieldError::getField)
                        .collect(Collectors.joining(", ")) + " 값들이 정확하지 않습니다.";
  }
}
