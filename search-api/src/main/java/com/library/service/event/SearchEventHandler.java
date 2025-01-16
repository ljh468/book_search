package com.library.service.event;

import com.library.entity.DailyStat;
import com.library.service.DailyStatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchEventHandler {

  private final DailyStatCommandService dailyStatCommandService;

  // 이벤트를 처리하는 Listener (별도의 쓰레드로 진행)
  @Async("bsExecutor")
  @EventListener
  public void handleEvent(SearchEvent event) {
    log.info("[SearchEventHandler] handleEvent: {}", event);
    dailyStatCommandService.save(new DailyStat(event.query(), event.timestamp()));
  }
}
