package com.asrcore.sis_cakes_menu.repository;

import com.asrcore.sis_cakes_menu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
