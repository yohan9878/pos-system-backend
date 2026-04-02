package com.pos.pos_system_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNo;
    private String outletId;
    private double total;
    private String date;
}