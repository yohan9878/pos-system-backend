package com.pos.pos_system_backend.service;

import com.pos.pos_system_backend.entity.Product;
import com.pos.pos_system_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product addProduct(Product product) {
        return repo.save(product);
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getByBarcode(Long barcode) {
        return repo.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Product not found with id " + id);
        }
        repo.deleteById(id);
    }
}
