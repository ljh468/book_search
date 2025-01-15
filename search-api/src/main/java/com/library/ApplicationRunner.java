package com.library;

import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ApplicationRunner implements CommandLineRunner {

  private final DailyStatRepository dailyStatRepository;

  @Override
  public void run(String... args) {
    DailyStat stat1 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 1, 1, 1));
    DailyStat stat2 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 3, 1, 1));
    DailyStat stat3 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 5, 1, 1));
    DailyStat stat4 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 6, 1, 1));
    DailyStat stat5 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 12, 1, 1));
    DailyStat stat6 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 16, 1, 1));
    DailyStat stat7 = new DailyStat("HTTP", LocalDateTime.of(2025, 1, 12, 23, 1, 1));

    DailyStat stat8 = new DailyStat("JAVA", LocalDateTime.of(2025, 1, 12, 3, 1, 1));
    DailyStat stat9 = new DailyStat("JAVA", LocalDateTime.of(2025, 1, 12, 14, 1, 1));

    DailyStat stat10 = new DailyStat("Kotlin", LocalDateTime.of(2025, 1, 1, 1, 1, 1));

    DailyStat stat11 = new DailyStat("Database", LocalDateTime.of(2025, 1, 2, 2, 2, 2));

    DailyStat stat12 = new DailyStat("OS", LocalDateTime.of(2025, 1, 3, 3, 3, 3));

    dailyStatRepository.saveAll(List.of(stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9,
                                        stat10, stat11, stat12, stat3));
  }
}
