package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.dto.SaleRequest;
import com.pos.pos_system_backend.entity.Sale;
import com.pos.pos_system_backend.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SaleService {

    private final SaleRepository repo;
    private final StockService stockService;

    public SaleService(SaleRepository repo, StockService stockService) {
        this.repo = repo;
        this.stockService = stockService;
    }

    @Transactional
    public void processSale(SaleRequest request) {

        request.getItems().forEach(item -> {
            stockService.reduceStock(
                    item.getBarcode(),
                    request.getOutletId(),
                    item.getQty()
            );
        });

        Sale sale = new Sale();
        sale.setInvoiceNo(request.getInvoiceNo());
        sale.setOutletId(request.getOutletId());
        sale.setTotal(request.getTotal());
        sale.setDate(LocalDate.now().toString());

        repo.save(sale);
    }

}
