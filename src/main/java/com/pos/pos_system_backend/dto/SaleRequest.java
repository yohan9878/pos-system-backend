package com.pos.pos_system_backend.dto;

import com.pos.pos_system_backend.PriceType;
import lombok.Data;
import java.util.List;

@Data
public class SaleRequest {
    private String invoiceNo;
    private String outletId;

    private List<SaleItem> items;

    public static class Item {
        private String barcode;
        private int qty;
        private double weight;
        private PriceType priceType;
    }

}
