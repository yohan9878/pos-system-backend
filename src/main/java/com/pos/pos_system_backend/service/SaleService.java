package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.dto.SaleItem;
import com.pos.pos_system_backend.dto.SaleRequest;
import com.pos.pos_system_backend.entity.Product;
import com.pos.pos_system_backend.entity.Sale;
import com.pos.pos_system_backend.repository.ProductRepository;
import com.pos.pos_system_backend.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository repo;
    private final StockService stockService;
    private final ProductRepository productRepo;

    public SaleService(SaleRepository repo, StockService stockService, ProductRepository productRepo) {
        this.repo = repo;
        this.stockService = stockService;
        this.productRepo = productRepo;
    }
    @Transactional
    public void processSale(SaleRequest req) {

        double subtotal = 0;

        Sale sale = new Sale();
        sale.setInvoiceNo(req.getInvoiceNo());
        sale.setOutletId(req.getOutletId());
        sale.setDiscountAmount(req.getDiscountAmount());
        sale.setDate(LocalDateTime.now());

        // attach items to sale
        for (SaleItem item : req.getItems()) {

            Product product = productRepo.findByBarcode(item.getBarcode())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            double itemTotal;

            if (product.isWeighted()) {
                itemTotal = item.getValue() * product.getPricePerKg();
            } else {
                double unitPrice = switch (item.getPriceType()) {
                    case BULK -> product.getBulkPrice();
                    case PACK -> product.getPackPrice();
                    default -> product.getRetailPrice();
                };
                itemTotal = unitPrice * item.getValue();
            }

            stockService.reduceStock(
                    item.getBarcode(),
                    req.getOutletId(),
                    item.getValue()
            );

            subtotal += itemTotal;

            item.setSale(sale);
        }

        sale.setTotal(subtotal - req.getDiscountAmount());

        sale.setItems(req.getItems());

        repo.save(sale);


    }

    public List<Sale> getSalesByDateAndOutletId(LocalDate date, String outletId) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        return repo.findByDateAndOutletId(start, end, outletId);
    }

}
