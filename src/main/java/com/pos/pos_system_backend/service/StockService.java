package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.entity.Product;
import com.pos.pos_system_backend.entity.Stock;
import com.pos.pos_system_backend.entity.StockHistory;
import com.pos.pos_system_backend.exception.InsufficientStockException;
import com.pos.pos_system_backend.repository.ProductRepository;
import com.pos.pos_system_backend.repository.StockHistoryRepository;
import com.pos.pos_system_backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository repo;
    private final ProductRepository productRepo;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    public StockService(StockRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public Stock addStock(Long barcode, String outletId, int qty, double weight, int thresholdQty, double thresholdWeight) {

        //Find product by barcode
        Product product = productRepo.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found with barcode: " + barcode));

        Optional<Stock> existingStockOpt = repo.findByBarcodeAndOutletId(barcode, outletId);


        if (existingStockOpt.isPresent()) {
            throw new RuntimeException("Stock already exists for this product in this outlet");
        }

        // Create stock
        Stock stock = new Stock();
        stock.setBarcode(product.getBarcode());
        stock.setProductName(product.getName()); // auto-fill productName
        stock.setWeighted(product.isWeighted());
        stock.setOutletId(outletId);
        stock.setWeight(weight);
        stock.setQuantity(qty);
        stock.setLowStockThresholdQty(thresholdQty);
        stock.setLowStockThresholdWeight(thresholdWeight);

//        if (product.isWeighted()) {
//            stock.setWeight(weight);
//            stock.setQuantity(0);
//            stock.setLowStockThresholdQty(0);
//            stock.setLowStockThresholdWeight(thresholdWeight);
//        } else {
//            stock.setWeight(0);
//            stock.setQuantity(qty);
//            stock.setLowStockThresholdQty(thresholdQty);
//            stock.setLowStockThresholdWeight(0);
//
//        }

        return repo.save(stock);
    }

    public void reduceStock(Long barcode, String outletId, int qty) {
        Stock stock = repo
                .findByBarcodeAndOutletId(barcode, outletId)
                .orElseThrow(() -> new RuntimeException(
                        "Stock not found for product " + barcode + " in outlet " + outletId
                ));

        if (stock.getQuantity() < qty) {
            throw new InsufficientStockException(
                    "Product " + barcode + " only has " + stock.getQuantity() + " items left"
            );
        }

        stock.setQuantity(stock.getQuantity() - qty);
        repo.save(stock);
    }

    public void reduceStockByWeight(Long barcode, String outletId, double weight) {
        Stock stock = repo.findByBarcodeAndOutletId(barcode, outletId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        stock.setWeight(stock.getWeight() - weight);
        repo.save(stock);
    }


    public List<Stock> getAllStock() {
        return repo.findAll();
    }

    public List<Stock> getStockByOutlet(String outletId) {
        return repo.findByOutletId(outletId);
    }

    //Stock Update int qty, double weight
    public Stock updateStock(Long id, double value, String user) {
        Optional<Stock> stockOpt = repo.findById(id);
        if (stockOpt.isEmpty()) {
            throw new RuntimeException("Stock not found with id " + id);
        }

//
//        Optional<Stock> stockOpt = repo.findByBarcodeAndOutletId(barcode, outletId);
//        if (stockOpt.isEmpty()) {
//            throw new RuntimeException("Stock not found with id " + barcode);
//        }

        Long barcode = stockOpt.get().getBarcode();
        Product product = productRepo.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found with barcode: " + barcode));

        //Update Stock
        Stock stock = stockOpt.get();

        //Update stock history
        StockHistory history = new StockHistory();
        history.setProductName(stock.getProductName());
        history.setBarcode(stock.getBarcode());
        history.setOutletId(stock.getOutletId());
        history.setChangedBy(user);
        history.setChangedAt(LocalDateTime.now());

        if (product.isWeighted()) {
            //update stock weight
            double oldWeight = stock.getWeight();
            double newWeight = oldWeight + value;
            stock.setWeight(newWeight);

            //update stock history
            history.setOldStock(oldWeight);
            history.setUpdatedStock(value);
            history.setNewStock(newWeight);

        } else {
            //update stock Qty
            int oldQty = stock.getQuantity();
            int newQty = oldQty + (int) value;
            stock.setQuantity(newQty);

            //update stock history
            history.setOldStock(oldQty);
            history.setUpdatedStock((int) value);
            history.setNewStock(newQty);
        }
        Stock updatedStock = repo.save(stock);
        stockHistoryRepository.save(history);
//        Stock stock = stockOpt.get();
//        int oldQty = stock.getQuantity();
//        int newQty = oldQty + quantity;
//        stock.setQuantity(newQty);
//        Stock updatedStock = repo.save(stock);


//        history.setProductName(stock.getProductName());
//        history.setBarcode(stock.getBarcode());
//        history.setOutletId(stock.getOutletId());
//        history.setOldQuantity(oldQty);
//        history.setUpdatedQty(quantity);
//        history.setNewQuantity(newQty);
//        history.setChangedBy(user);
//        history.setChangedAt(LocalDateTime.now());


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