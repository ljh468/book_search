package com.library.util

import spock.lang.Specification

import java.time.LocalDate

class DateUtilsTest extends Specification {
  def "문자열(yyyyMMdd)을 LocalDate 객체로 변환한다."() {
    given:
    def date = "20240101"

    when:
    def result = DateUtils.parseYYYYMMDD(date)

    then:
    result == LocalDate.of(2024, 1, 1)
  }
}
