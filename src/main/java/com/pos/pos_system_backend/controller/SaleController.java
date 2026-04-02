package com.pos.pos_system_backend.controller;


import com.pos.pos_system_backend.dto.SaleRequest;
import com.pos.pos_system_backend.service.SaleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public String processSale(@RequestBody SaleRequest request) {
        service.processSale(request);
        return "Sale completed";
    }
}