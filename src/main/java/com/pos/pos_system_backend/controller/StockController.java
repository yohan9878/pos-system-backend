package com.pos.pos_system_backend.controller;

import com.pos.pos_system_backend.dto.StockRequest;
import com.pos.pos_system_backend.entity.Stock;
import com.pos.pos_system_backend.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                req.getBarcode(),
                req.getOutletId(),
                req.getQuantity(),
                req.getWeight(),
                req.getLowStockThresholdQty(),
                req.getLowStockThresholdWeight()
        );
    }

    @GetMapping
    public List<Stock> getStock(
            @RequestParam(required = false) String outletId
    ) {
        if (outletId != null) {
            return service.getStockByOutlet(outletId);
        }
        return service.getAllStock();
    }

    @PutMapping("/{barcode}")
    public Stock updateStock(
            @PathVariable Long barcode,
            @RequestBody StockRequest req
    ) {
        return service.updateStock(
                barcode,
                req.getOutletId(),
                req.getQuantity(),
                req.getWeight(),
                req.getUser()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        service.deleteStock(id);
    }
}
