package com.library.repository

import com.library.entity.DailyStat
import com.library.feign.NaverFeignClient
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
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
  NaverFeignClient naverFeignClient = Mock()

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
}
