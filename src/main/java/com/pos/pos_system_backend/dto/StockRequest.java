package com.pos.pos_system_backend.dto;

import lombok.Data;

@Data
public class StockRequest {
    private Long productId;
    private String outletId;
    private int quantity;
}