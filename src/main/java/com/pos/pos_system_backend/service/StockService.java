package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.entity.Stock;
import com.pos.pos_system_backend.entity.StockHistory;
import com.pos.pos_system_backend.exception.InsufficientStockException;
import com.pos.pos_system_backend.repository.StockHistoryRepository;
import com.pos.pos_system_backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository repo;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    public StockService(StockRepository repo) {
        this.repo = repo;
    }

    public Stock addStock(Long barcode, String productName, String outletId, int qty) {
        Stock stock = repo
                .findByBarcodeAndOutletId(barcode, outletId)
                .orElse(new Stock());

        stock.setBarcode(barcode);
        stock.setProductName(productName);
        stock.setOutletId(outletId);
        stock.setQuantity(stock.getQuantity() + qty);

        return repo.save(stock);
    }

    public void reduceStock(Long barcode, String outletId, int qty) {
        Stock stock = repo
                .findByBarcodeAndOutletId(barcode, outletId)
                .orElseThrow(() -> new RuntimeException(
                        "Stock not found for product " + barcode + " in outlet " + outletId
                ));

        if (stock.getQuantity() < qty) {
//            throw new RuntimeException("Not enough stock");
            throw new InsufficientStockException(
                    "Product " + barcode + " only has " + stock.getQuantity() + " items left"
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

    public Stock updateStockQuantity(Long id, int quantity, String user) {
        Optional<Stock> stockOpt = repo.findById(id);
        if (stockOpt.isEmpty()) {
            throw new RuntimeException("Stock not found with id " + id);
        }

        //Update Stock
        Stock stock = stockOpt.get();
        int oldQty = stock.getQuantity();
        int newQty = oldQty + quantity;
        stock.setQuantity(newQty);
        Stock updatedStock = repo.save(stock);

        //Update stock history
        StockHistory history = new StockHistory();
        history.setProductName(stock.getProductName());
        history.setBarcode(stock.getBarcode());
        history.setOutletId(stock.getOutletId());
        history.setOldQuantity(oldQty);
        history.setUpdatedQty(quantity);
        history.setNewQuantity(newQty);
        history.setChangedBy(user);
        history.setChangedAt(LocalDate.now());

        stockHistoryRepository.save(history);

        return updatedStock;
    }

    public void deleteStock(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Stock not found with id " + id);
        }
        repo.deleteById(id);
    }


    //Stock History
    public List<StockHistory> getAllHistory() {
        return stockHistoryRepository.findAll(
                Sort.by(Sort.Direction.DESC, "changedAt")
        );
    }
}