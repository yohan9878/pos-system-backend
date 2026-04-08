package com.pos.pos_system_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Long barcode;
    private String outletId;

    private int oldQuantity;
    private int updatedQty;
    private int newQuantity;

    private String changedBy;

    private LocalDate changedAt;
}