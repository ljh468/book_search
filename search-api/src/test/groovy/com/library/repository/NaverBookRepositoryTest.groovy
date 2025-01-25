package com.library.repository

import com.library.Item
import com.library.NaverBookResponse
import com.library.feign.NaverClient
import spock.lang.Specification

import java.time.LocalDate

class NaverBookRepositoryTest extends Specification {

  BookRepository bookRepository

  NaverClient naverClient = Mock()

  void setup() {
    bookRepository = new NaverBookRepository(naverClient)
  }

  def "search 호출시 적절한 데이터형식으로 변환한다."() {
    given:
    def items = [
        new Item(title: "제목1", author: "저자1", publisher: "출판사1", pubDate: "20240101", isbn: "isbn1"),
        new Item(title: "제목2", author: "저자2", publisher: "출판사2", pubDate: "20240101", isbn: "isbn2")
    ]

    def response = new NaverBookResponse(
        lastBuildDate: "Wed, 29 May 2024 21:12:29 +0900",
        total: 2,
        start: 1,
        display: 2,
        items: items
    )

    and:
    1 * naverClient.search("HTTP", 1, 2) >> response

    when:
    def result = bookRepository.search("HTTP", 1, 2)

    then:
    verifyAll {
      result.page() == 1
      result.size() == 2
      result.totalElements() == 2
      result.contents().size() == 2
      result.contents().get(0).pubDate() == LocalDate.of(2024, 1, 1)
    }
  }
}