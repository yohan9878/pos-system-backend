package com.pos.pos_system_backend.repository;

import com.pos.pos_system_backend.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT COALESCE(SUM(s.total),0) FROM Sale s WHERE s.outletId = :outletId AND s.date = :date")
    double getTotalSales(@Param("outletId") String outletId, @Param("date") String date);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.outletId = :outletId AND s.date = :date")
    long getTotalTransactions(@Param("outletId") String outletId, @Param("date") String date);
}