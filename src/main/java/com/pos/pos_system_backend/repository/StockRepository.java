package com.pos.pos_system_backend.repository;

import com.pos.pos_system_backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductIdAndOutletId(Long productId, String outletId);
    List<Stock> findByOutletId(String outletId);
}