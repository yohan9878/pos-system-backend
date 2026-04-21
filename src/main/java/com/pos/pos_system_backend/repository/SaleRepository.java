package com.pos.pos_system_backend.repository;

import com.pos.pos_system_backend.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT COALESCE(" + "SUM(s.total),0) " + "FROM Sale s " + "WHERE s.outletId = :outletId AND s.date BETWEEN :start AND :end")
    double getTotalSales(@Param("outletId") String outletId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.outletId = :outletId AND s.date BETWEEN :start AND :end")
    long getTotalTransactions(@Param("outletId") String outletId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

//    List<Sale> findByDate(String date);
//
//    List<Sale> findByOutletIdAndDate(String outletId, String date);

    @Query("SELECT DISTINCT s.outletId FROM Sale s WHERE s.date >= :start AND s.date < :end")
    List<String> findDistinctOutletIdsBetween( @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
}