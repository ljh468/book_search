package com.library.feign

import feign.RequestTemplate
import spock.lang.Specification

// requestTemplate에 헤더값이 제대로 들어갔는지 테스트
class NaverFeignClientConfigurationTest extends Specification {

  NaverFeignClientConfiguration configuration

  void setup() {
    configuration = new NaverFeignClientConfiguration()
  }

  def "requestInterceptor의 header에 key값들이 적용된다."() {
    given:
    def template = new RequestTemplate()
    def clientId = "id"
    def clientSecret = "secret"

    and: "interceptor를 타기전에 header가 존재하지 않는다."
    template.headers()["X-Naver-Client-Id"] == null
    template.headers()["X-Naver-Client-Secret"] == null

    when: "interceptor를 탄다."
    def interceptor = configuration.requestInterceptor(clientId, clientSecret)
    interceptor.apply(template)

    // 반환값이나 객체의 상태를 확인
    then: "interceptor를 탄 이후에는 hearder가 추가된다."
    template.headers()["X-Naver-Client-Id"].contains(clientId)
    template.headers()["X-Naver-Client-Secret"].contains(clientSecret)
  }
}
