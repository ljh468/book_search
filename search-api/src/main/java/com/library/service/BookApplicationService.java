package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.controller.response.StatResponse;
import com.library.entity.DailyStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 비즈니스 담당하는 서비스
@RequiredArgsConstructor
@Service
public class BookApplicationService {

  private final BookQueryService bookQueryService;

  private final DailyStatCommandService dailyStatCommandService;

  private final DailyStatQueryService dailyStatQueryService;

  public PageResult<SearchResponse> search(String query, int page, int size) {
    // 1. 외부 api 호출
    PageResult<SearchResponse> response = bookQueryService.search(query, page, size);
    // 2. 통계 데이터 저장
    dailyStatCommandService.save(new DailyStat(query, LocalDateTime.now()));
    // 3. 조회 데이터 응답
    return response;
  }

  public StatResponse findQueryCount(String query, LocalDate date) {
    return dailyStatQueryService.findQueryCount(query, date);
  }

  public List<StatResponse> findTop5Query() {
    return dailyStatQueryService.findTop5Query();
  }
}
