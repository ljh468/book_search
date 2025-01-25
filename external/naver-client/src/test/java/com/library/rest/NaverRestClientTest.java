package com.library.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest(classes = NaverRestClientTest.TestConfig.class) // 테스트와 함께 로드할 클래스 지정
@ActiveProfiles("test") // "test" 프로파일을 활성화하도록 지정
class NaverRestClientTest {

  // basePackageClasses 속성: NaverRestClient 클래스가 속한 패키지를 스캔하도록 설정
  @ComponentScan(basePackageClasses = NaverRestClient.class)
  static class TestConfig {}

  // 의존성 주입
  @Autowired
  NaverRestClient naverRestClient;

  @Test
  @DisplayName("naverRestClient를 호출하여 HTTP 키워드로 책을 조회한다.")
  void callNaver() {
    String http = naverRestClient.search("HTTP");
    System.out.println("http = " + http);
  }

}