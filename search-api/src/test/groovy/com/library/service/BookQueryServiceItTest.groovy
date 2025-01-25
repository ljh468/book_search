package com.library.service

import com.library.controller.response.PageResult
import com.library.repository.KakaoBookRepository
import com.library.repository.NaverBookRepository
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

// 각 테스트 메서드 실행 후 스프링 컨텍스트를 초기화하도록 지정
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class BookQueryServiceItTest extends Specification {

  @Autowired
  BookQueryService bookQueryService

  // 서킷의 상태를 검증하기 위한 레지스트리
  @Autowired
  CircuitBreakerRegistry circuitBreakerRegistry

  @SpringBean
  KakaoBookRepository kakaoBookRepository = Mock()

  @SpringBean
  NaverBookRepository naverBookRepository = Mock()

  def "Circuit의 상태가 CLOSED이면 naver search API를 호출한다."() {
    given:
    def query = 'HTTP'
    def page = 1
    def size = 10

    when:
    bookQueryService.search(query, page, size)

    then:
    1 * naverBookRepository.search(query, page, size) >>
        new PageResult<>(1, 10, 0, [])

    and:
    def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers()
        .stream()
        .findFirst()
        .get()
    circuitBreaker.state == CircuitBreaker.State.CLOSED

    and:
    0 * kakaoBookRepository.search()
  }

  def "Circuit의 상태가 OPEN이면 kakao search API를 호출한다."() {
    given:
    def query = 'HTTP'
    def page = 1
    def size = 10
    def kakaoResponse = new PageResult<>(1, 10, 0, [])

    // 서킷이 바로 열릴 수 있게 설정을 변경
    def config = CircuitBreakerConfig.custom()
        .slidingWindowSize(1)
        .minimumNumberOfCalls(1)
        .failureRateThreshold(50)
        .build()
    circuitBreakerRegistry.circuitBreaker("naverSearch", config)

    and: "naver 검색 API 호출시 항상 예외가 발생한다."
    naverBookRepository.search(query, page, size) >> { throw new RuntimeException("error!") }

    when:
    def result = bookQueryService.search(query, page, size)

    then: "kakao search API로 Fallback 된다."
    1 * kakaoBookRepository.search(query, page, size) >> kakaoResponse

    and: "circuit이 OPEN 된다."
    def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers()
        .stream()
        .findFirst()
        .get()
    circuitBreaker.state == CircuitBreaker.State.OPEN

    and:
    result == kakaoResponse
  }
}
