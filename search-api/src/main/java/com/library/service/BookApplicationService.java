package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.controller.response.StatResponse;
import com.library.service.event.SearchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 비즈니스 담당하는 서비스
@Slf4j
@RequiredArgsConstructor
@Service
public class BookApplicationService {

  private final BookQueryService bookQueryService;

  private final DailyStatQueryService dailyStatQueryService;

  private final ApplicationEventPublisher eventPublisher;

  public PageResult<SearchResponse> search(String query, int page, int size) {
    PageResult<SearchResponse> response = bookQueryService.search(query, page, size);
    if (!response.contents().isEmpty()) {
      log.info("검색결과 개수: {}", response.size());
      // 비동기 이벤트를 발행하여 EventHandler가 처리하게 함
      eventPublisher.publishEvent(new SearchEvent(query, LocalDateTime.now()));
    }
    return response;
  }

  public StatResponse findQueryCount(String query, LocalDate date) {
    return dailyStatQueryService.findQueryCount(query, date);
  }

  public List<StatResponse> findTop5Query() {
    return dailyStatQueryService.findTop5Query();
  }
}
