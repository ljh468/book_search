package com.library.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class NaverErrorDecoderTestWithJunit {

  private ObjectMapper objectMapper;
  private NaverErrorDecoder errorDecoder;

  @BeforeEach
  void setUp() {
    objectMapper = mock(ObjectMapper.class);
    errorDecoder = new NaverErrorDecoder(objectMapper);
  }

  @Test
  @DisplayName("에러 디코더에서 에러 발생 시 RuntimeException 예외가 발생한다.")
  void testDecode_throwsRuntimeException() throws Exception {
    Response.Body responseBody = mock(Response.Body.class);

    // Mock된 Response.Body와 InputStream 준비
    String json = "{\"errorMessage\":\"error!!\",\"errorCode\":\"SE03\"}";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

    // Response 객체 생성
    Request request = Request.create(Request.HttpMethod.GET, "testUrl", Collections.emptyMap(), (Request.Body) null, null);
    Response response = Response.builder()
                                .status(400)
                                .request(request)
                                .body(responseBody)
                                .build();

    // Response.Body와 ObjectMapper 동작
    when(responseBody.asInputStream()).thenReturn(inputStream);
    when(objectMapper.readValue(anyString(), eq(NaverErrorResponse.class)))
        .thenReturn(new NaverErrorResponse("error!!", "SE03"));


    // 테스트 실행 및 검증
    RuntimeException exception = assertThrows(RuntimeException.class, () -> errorDecoder.decode("string", response));
    assertEquals("error!!", exception.getMessage());

    // verify: 각 메서드가 1번 호출되었는지 확인
    verify(responseBody, times(1)).asInputStream(); // responseBody.asInputStream()가 한 번 호출되었는지 확인
    verify(objectMapper, times(1)).readValue(anyString(), eq(NaverErrorResponse.class)); // objectMapper.readValue()가 한 번 호출되었는지 확인
  }
}
