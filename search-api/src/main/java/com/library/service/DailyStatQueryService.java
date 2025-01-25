package com.library.service;

import com.library.controller.response.StatResponse;
import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyStatQueryService {

  private static final int PAGE_NUMBER = 0;

  private static final int PAGE_SIZE = 5;

  private final DailyStatRepository dailyStatRepository;

  @Transactional
  public StatResponse findQueryCount(String query, LocalDate date) {
    long count = dailyStatRepository.countByQueryAndEventDateTimeBetween(
        query,
        date.atStartOfDay(),
        date.atTime(LocalTime.MAX)
    );
    return new StatResponse(query, count);
  }

  // StatResponse 최종응답까지 나갈 클래스라 레이어관점에서 부적절함
  public List<StatResponse> findTop5Query() {
    Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
    return dailyStatRepository.findTopByQuery(pageable);
  }
}
