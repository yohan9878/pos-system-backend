package com.pos.pos_system_backend.repository;

import com.pos.pos_system_backend.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}