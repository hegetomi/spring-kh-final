package com.hegetomi.orderservice.repository;

import com.hegetomi.orderservice.model.AppOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppOrderRepository extends JpaRepository<AppOrder, Long> {
    List<AppOrder> findAllByUsername(String username);

    @EntityGraph(attributePaths = {"orderedProductList"})
    @Query("Select a from AppOrder a where a.id in :ids")
    List<AppOrder> findAllWithProducts(List<Long> ids);

    @EntityGraph(attributePaths = {"orderedProductList"})
    @Query("Select a from AppOrder a where a.id= :id")
    List<AppOrder> findWithProducts(Long id);
}