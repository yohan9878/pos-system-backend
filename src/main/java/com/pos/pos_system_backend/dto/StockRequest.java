package com.pos.pos_system_backend.dto;

import lombok.Data;

@Data
public class StockRequest {
    private Long id;
    private Long barcode;
    private String outletId;
    private int quantity;
    private String user;
}