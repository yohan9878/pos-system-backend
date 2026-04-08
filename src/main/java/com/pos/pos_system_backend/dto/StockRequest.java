package com.pos.pos_system_backend.dto;

import lombok.Data;

@Data
public class StockRequest {
    private Long barcode;
    private String productName;
    private String outletId;
    private String user;
    private int quantity;
}