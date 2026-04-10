package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.dto.SaleItem;
import com.pos.pos_system_backend.dto.SaleRequest;
import com.pos.pos_system_backend.entity.Product;
import com.pos.pos_system_backend.entity.Sale;
import com.pos.pos_system_backend.repository.ProductRepository;
import com.pos.pos_system_backend.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

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
    public void processSale(SaleRequest request) {

        double total = 0;

//        request.getItems().forEach(item -> {
//            stockService.reduceStock(
//                    item.getBarcode(),
//                    request.getOutletId(),
//                    item.getQty()
//            );
//        });
//
//        Sale sale = new Sale();
//        sale.setInvoiceNo(request.getInvoiceNo());
//        sale.setOutletId(request.getOutletId());
//        sale.setTotal(request.getTotal());
//        sale.setDate(LocalDate.now().toString());
//
//        repo.save(sale);
//    }
        for (SaleItem item : request.getItems()) {

            Product product = productRepo.findById(item.getBarcode())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            double itemTotal;

            //WEIGHT-BASED (Chicken)
            if (product.isWeighted()) {

                itemTotal = (item.getWeight() / 1000) * product.getPricePerKg();

                // 🔥 Deduct stock by weight
                stockService.reduceStockByWeight(
                        item.getBarcode(),
                        request.getOutletId(),
                        item.getWeight()
                );

            } else {

                double unitPrice = switch (item.getPriceType()) {
                    case PACK -> product.getPackPrice();
                    case BULK -> product.getBulkPrice();
                    default -> product.getRetailPrice();
                };

                itemTotal = unitPrice * item.getQuantity();

                //Deduct stock by quantity
                stockService.reduceStock(
                        item.getBarcode(),
                        request.getOutletId(),
                        item.getQuantity()
                );
            }

            total += itemTotal;
        }

        //Save sale (backend-calculated total)
        Sale sale = new Sale();
        sale.setInvoiceNo(request.getInvoiceNo());
        sale.setOutletId(request.getOutletId());
        sale.setTotal(total);
        sale.setDate(LocalDateTime.now());

        repo.save(sale);
    }

}
