package com.hegetomi.orderservice.repository;

import com.hegetomi.orderservice.model.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}