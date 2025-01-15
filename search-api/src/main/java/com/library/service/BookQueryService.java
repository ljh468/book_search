package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.repository.BookRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookQueryService {

  // 스프링에서 다형성을 가진 빈(Bean) 중 특정 빈을 주입할 때 사용하는 애노테이션
  // @RequiredArgsConstructor에 Qualifier 에노테이션을 복사할 수 있도록 하려면 설정을 추가해야함 - lombok.config
  @Qualifier("naverBookRepository")
  private final BookRepository naverBookRepository;

  @Qualifier("kakaoBookRepository")
  private final BookRepository kakaoBookRepository;

  // 문제가 발생할 수 있는 곳에 서킷브레이커 적용
  @CircuitBreaker(name = "naverSearch", fallbackMethod = "searchFallBack")
  public PageResult<SearchResponse> search(String query, int page, int size) {
    return naverBookRepository.search(query, page, size);
  }

  // search 메서드에서 에러가 발생하면 실행
  public PageResult<SearchResponse> searchFallBack(String query, int page, int size, Throwable throwable) {
    // 서킷이 열렸을 때의 에러인지, 단순 에러인지 구분해서 처리
    if (throwable instanceof CallNotPermittedException) {
      return handleOpenCircuit(query, page, size);
    }
    return handleException(query, page, size, throwable);
  }

  // 서킷이 열렸을 때 처리 (카카오 API 호출)
  private PageResult<SearchResponse> handleOpenCircuit(String query, int page, int size) {
    log.warn("[BookQueryService] Circuit Breaker is open! Fallback to kakao search.");
    return kakaoBookRepository.search(query, page, size);
  }

  // 서킷이 닫혔을때, 단순 에러 처리
  private PageResult<SearchResponse> handleException(String query, int page, int size, Throwable throwable) {
    // 임의로 단순에러도 카카오 검색 API 호출하도록 추가
    log.error("[BookQueryService] An error occurred! Fallback to kakao search. errorMessage={}", throwable.getMessage());
    return kakaoBookRepository.search(query, page, size);
  }
}
