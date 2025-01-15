package com.library.repository;

import com.library.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {

  long countByQueryAndEventDateTimeBetween(String query, LocalDateTime start, LocalDateTime end);
}
