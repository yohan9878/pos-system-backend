package com.pos.pos_system_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long barcode;
    private String productName;

//  @ManyToOne
//  @JoinColumn(name = "barcode")
//  @JoinColumn(name= "name")
//  private Product product;
    private String outletId;
    private int quantity;
}