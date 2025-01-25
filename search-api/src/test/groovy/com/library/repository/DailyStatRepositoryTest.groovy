package com.library.repository

import com.library.entity.DailyStat
import com.library.feign.KakaoClient
import com.library.feign.NaverClient
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@ActiveProfiles("test")
@DataJpaTest
class DailyStatRepositoryTest extends Specification {

  @Autowired
  DailyStatRepository dailyStatRepository

  @Autowired
  EntityManager entityManager

  @SpringBean
  NaverClient naverClient = Mock()

  @SpringBean
  KakaoClient kakaoClient = Mock()

  def "데이터를 DB에 저장 후 조회한다."() {
    given:
    def givenQuery = "HTTP"

    when: "DailyStat 데이터를 save하고 flush한다."
    def dailyStat = new DailyStat(givenQuery, LocalDateTime.now())
    def saved = dailyStatRepository.saveAndFlush(dailyStat)

    then: "실제 저장이 된다."
    saved.id != null

    when: "entityManager를 clear하고 실제 DB에서 쿼리로 조회한다."
    entityManager.clear()
    def result = dailyStatRepository.findById(saved.id)

    then: "데이터를 올바르게 조회되었는지 확인한다."
    verifyAll {
      result.isPresent()
      result.get().query == givenQuery
    }
  }

  def "쿼리의 카운트를 조회한다."() {
    given:
    def givenQuery = 'HTTP'
    def now = LocalDateTime.of(2024, 5, 2, 0, 0, 0)

    def stat1 = new DailyStat(givenQuery, now.plusMinutes(10))
    def stat2 = new DailyStat(givenQuery, now.minusMinutes(10))
    def stat3 = new DailyStat(givenQuery, now.plusHours(5))
    def stat4 = new DailyStat('JAVA', now.plusMinutes(10))

    dailyStatRepository.saveAll([stat1, stat2, stat3, stat4])

    when:
    def result = dailyStatRepository.countByQueryAndEventDateTimeBetween(givenQuery, now, now.plusDays(1))

    then:
    result == 2
  }

  def "가장 많이 검색된 상위 3개의 쿼리 키워드를 반환한다."() {
    given:
    def now = LocalDateTime.now()
    def stat1 = new DailyStat('HTTP', now.plusMinutes(10))
    def stat2 = new DailyStat('HTTP', now.plusMinutes(10))
    def stat3 = new DailyStat('HTTP', now.plusMinutes(10))
    def stat4 = new DailyStat('JAVA', now.plusMinutes(10))
    def stat5 = new DailyStat('JAVA', now.plusMinutes(10))
    def stat6 = new DailyStat('JAVA', now.plusMinutes(10))
    def stat7 = new DailyStat('JAVA', now.plusMinutes(10))
    def stat8 = new DailyStat('SPRING', now.plusMinutes(10))
    def stat9 = new DailyStat('SPRING', now.plusMinutes(10))
    def stat10 = new DailyStat('OS', now.plusMinutes(10))
    def stat11 = new DailyStat('NETWORK', now.plusMinutes(10))
    dailyStatRepository.saveAll([stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9, stat10, stat11])

    when:
    def request = PageRequest.of(0, 3)
    def response = dailyStatRepository.findTopByQuery(request)


    then:
    verifyAll {
      response.size() == 3
      response[0].query() == 'JAVA'
      response[0].count() == 4

      response[1].query() == 'HTTP'
      response[1].count() == 3

      response[2].query() == 'SPRING'
      response[2].count() == 2
    }
  }

}
