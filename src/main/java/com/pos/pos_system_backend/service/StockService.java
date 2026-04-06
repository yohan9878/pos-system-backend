package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.entity.Stock;
import com.pos.pos_system_backend.exception.InsufficientStockException;
import com.pos.pos_system_backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository repo;

    public StockService(StockRepository repo) {
        this.repo = repo;
    }

    public Stock addStock(Long productId, String productName, String outletId, int qty) {
        Stock stock = repo
                .findByProductIdAndOutletId(productId, outletId)
                .orElse(new Stock());

        stock.setProductId(productId);
        stock.setProductName(productName);
        stock.setOutletId(outletId);
        stock.setQuantity(stock.getQuantity() + qty);

        return repo.save(stock);
    }

    public void reduceStock(Long productId, String outletId, int qty) {
        Stock stock = repo
                .findByProductIdAndOutletId(productId, outletId)
                .orElseThrow(() -> new RuntimeException(
                        "Stock not found for product " + productId + " in outlet " + outletId
                ));

        if (stock.getQuantity() < qty) {
//            throw new RuntimeException("Not enough stock");
            throw new InsufficientStockException(
                    "Product " + productId + " only has " + stock.getQuantity() + " items left"
            );
        }

        stock.setQuantity(stock.getQuantity() - qty);
        repo.save(stock);
    }


    public List<Stock> getAllStock() {
        return repo.findAll();
    }

    public List<Stock> getStockByOutlet(String outletId) {
        return repo.findByOutletId(outletId);
    }

    public Stock updateStockQuantity(Long id, Integer quantity) {
        Optional<Stock> stockOpt = repo.findById(id);
        if (stockOpt.isEmpty()) {
            throw new RuntimeException("Stock not found with id " + id);
        }
        Stock stock = stockOpt.get();
        stock.setQuantity(quantity);
        return repo.save(stock);
    }

    public void deleteStock(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Stock not found with id " + id);
        }
        repo.deleteById(id);
    }
}