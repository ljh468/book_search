package com.library.service;

import com.library.controller.response.StatResponse;
import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyStatQueryService {

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
}
