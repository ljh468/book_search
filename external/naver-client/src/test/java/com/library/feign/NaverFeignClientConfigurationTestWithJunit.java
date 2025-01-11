package com.library.feign;

import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
public class NaverFeignClientConfigurationTestWithJunit {

  NaverFeignClientConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new NaverFeignClientConfiguration();
  }

  @Test
  @DisplayName("requestInterceptor의 header에 key값들이 적용된다.")
  void testRequestInterceptorAddsHeaders() {
    // given
    RequestTemplate template = new RequestTemplate();
    String clientId = "id";
    String clientSecret = "secret";

    // Ensure headers are empty initially
    assertNull(template.headers().get("X-Naver-Client-Id"));
    assertNull(template.headers().get("X-Naver-Client-Secret"));

    // when
    var interceptor = configuration.requestInterceptor(clientId, clientSecret);
    interceptor.apply(template);

    // then
    assertTrue(template.headers().get("X-Naver-Client-Id").contains(clientId));
    assertTrue(template.headers().get("X-Naver-Client-Secret").contains(clientSecret));
  }
}
