package com.library.feign;

import com.library.NaverBookResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@SpringBootTest(classes = NaverFeignClientIntegrationTestWithJunit.TestConfig.class) // 테스트와 함께 로드할 클래스 지정
@ActiveProfiles("test")
class NaverFeignClientIntegrationTestWithJunit {

  // Feign 설정들을 자동으로 로드
  @EnableAutoConfiguration
  // FeignClient 인터페이스를 스캔하여 자동으로 구현체를 생성하고 스프링 빈으로 등록하는 역할
  @EnableFeignClients(clients = NaverFeignClient.class)
  static class TestConfig {}

  @Autowired
  NaverFeignClient naverFeignClient;

  @Test
  @DisplayName("naverFeignClient를 호출하여 HTTP 키워드로 책을 조회한다.")
  void callNaver() {
    NaverBookResponse http = naverFeignClient.search("HTTP", 1, 10);
    System.out.println("http = " + http);

    assertNotNull(http);
  }
}