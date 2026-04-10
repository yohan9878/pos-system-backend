package com.pos.pos_system_backend.entity;

import com.pos.pos_system_backend.dto.SaleItem;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNo;
    private String outletId;
    private double total;

    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SaleItem> items;

}