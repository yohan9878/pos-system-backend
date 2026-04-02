package com.pos.pos_system_backend.controller;

import com.pos.pos_system_backend.entity.Product;
import com.pos.pos_system_backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping()
    public Product add(@RequestBody Product product) {
        return service.addProduct(product);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{barcode}")
    public Product getByBarcode(@PathVariable String barcode) {
        return service.getByBarcode(barcode);
    }
}
