package com.pos.pos_system_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class SaleRequest {
    private String invoiceNo;
    private String outletId;
    private double total;
    private String date;

    private List<SaleItem> items;


}
