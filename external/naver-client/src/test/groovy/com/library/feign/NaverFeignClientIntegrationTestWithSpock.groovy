package com.library.feign

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.test.context.ActiveProfiles
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
@SpringBootTest(classes = NaverFeignClientIntegrationTestWithSpock.TestConfig.class)
@ActiveProfiles("test")
class NaverFeignClientIntegrationTestWithSpock extends Specification {

  @EnableAutoConfiguration
  @EnableFeignClients(clients = NaverFeignClient.class)
  static class TestConfig {}

  @Autowired
  NaverFeignClient naverFeignClient

  def "naver 호출"() {
    given:

    when:
    def response = naverFeignClient.search("HTTP", 1, 10)

    then:
    print response
  }
}
