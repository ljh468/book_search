package com.library.repository;

import com.library.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
}
