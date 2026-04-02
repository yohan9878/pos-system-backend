package com.pos.pos_system_backend.controller;

import com.pos.pos_system_backend.dto.StockRequest;
import com.pos.pos_system_backend.entity.Stock;
import com.pos.pos_system_backend.service.StockService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping()
    public Stock addStock(@RequestBody StockRequest req) {
        return service.addStock(
                req.getProductId(),
                req.getOutletId(),
                req.getQuantity()
        );
    }
}
