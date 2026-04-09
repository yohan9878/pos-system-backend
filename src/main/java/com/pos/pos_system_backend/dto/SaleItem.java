package com.pos.pos_system_backend.dto;

import lombok.Data;

@Data
public class SaleItem {
    private Long barcode;
    private int qty;
}