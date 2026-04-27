package com.pos.pos_system_backend.repository;

import com.pos.pos_system_backend.entity.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    List<StockHistory> findByChangedAtAfterOrderByChangedAtDesc(LocalDateTime date);
}

