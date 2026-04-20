package com.pos.pos_system_backend.dto;

import com.pos.pos_system_backend.PriceType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long barcode;
    private String productName;

    private double value;

    private double unitPrice;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;
}