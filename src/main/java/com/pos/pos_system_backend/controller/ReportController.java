package com.pos.pos_system_backend.controller;

import com.pos.pos_system_backend.dto.DailyReportResponse;
import com.pos.pos_system_backend.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/daily")
    public List<DailyReportResponse> getDailyReport(
            @RequestParam String date,
            @RequestParam(required = false) String outletId
    ) {
        return service.getDailyReport(date, outletId);
    }
}