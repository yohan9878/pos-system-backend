package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.entity.Stock;
import com.pos.pos_system_backend.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository repo;

    public StockService(StockRepository repo) {
        this.repo = repo;
    }

    public Stock addStock(Long productId, String outletId, int qty) {
        Stock stock = repo
                .findByProductIdAndOutletId(productId, outletId)
                .orElse(new Stock());

        stock.setProductId(productId);
        stock.setOutletId(outletId);
        stock.setQuantity(stock.getQuantity() + qty);

        return repo.save(stock);
    }

    public void reduceStock(Long productId, String outletId, int qty) {
        Stock stock = repo
                .findByProductIdAndOutletId(productId, outletId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (stock.getQuantity() < qty) {
            throw new RuntimeException("Not enough stock");
        }

        stock.setQuantity(stock.getQuantity() - qty);
        repo.save(stock);
    }
}