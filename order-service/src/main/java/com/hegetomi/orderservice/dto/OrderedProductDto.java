package com.hegetomi.orderservice.dto;

import com.hegetomi.orderservice.model.AppOrder;
import lombok.Data;

import javax.persistence.ManyToOne;

@Data
public class OrderedProductDto {
    private Long id;
    private String productName;
    private String categoryName;
    private Long unitPrice;
    private Long quantity;
    private AppOrderDto appOrder;
}
