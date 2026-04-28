package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.dto.DailyReportResponse;
import com.pos.pos_system_backend.dto.SoldItemReport;
import com.pos.pos_system_backend.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final SaleRepository repo;

    public ReportService(SaleRepository repo) {
        this.repo = repo;
    }

    public List<DailyReportResponse> getDailyReport(String date, String outletId) {

        LocalDate localDate = LocalDate.parse(date);

        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.plusDays(1).atStartOfDay();

        if (outletId != null) {
            return List.of(buildReport(outletId, start, end, date));
        }

        // ALL OUTLETS (FIXED)
        List<String> outlets = repo.findDistinctOutletIdsBetween(start, end);

        return outlets.stream().map(o -> buildReport(o, start, end, date)).toList();
    }

    public DailyReportResponse buildReport(String outletId, LocalDateTime start, LocalDateTime end, String date) {

        double totalDiscount = repo.getTotalDiscount(outletId,start, end);
        double totalSales = repo.getTotalSales(outletId, start, end);
        long totalTransactions = repo.getTotalTransactions(outletId, start, end);

        DailyReportResponse res = new DailyReportResponse();
        res.setDate(date);
        res.setOutletId(outletId);
        res.setDiscountAmount(totalDiscount);
        res.setTotalSales(totalSales);
        res.setTotalTransactions(totalTransactions);

        return res;
    }


    public List<SoldItemReport> getSoldItems(String outletId, String date) {

        LocalDate localDate = LocalDate.parse(date);

        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.plusDays(1).atStartOfDay();

        List<Object[]> results = repo.getSoldItemsReport(outletId, start, end);

        return results.stream().map(r -> {
            SoldItemReport dto = new SoldItemReport();
            dto.setBarcode(String.valueOf(r[0]));
            dto.setItemName((String) r[1]);
            dto.setSaleQty(((Number) r[2]).doubleValue());
            dto.setSalePrice(((Number) r[3]).doubleValue());
            dto.setSaleValue(((Number) r[4]).doubleValue());
            return dto;
        }).toList();
    }
}