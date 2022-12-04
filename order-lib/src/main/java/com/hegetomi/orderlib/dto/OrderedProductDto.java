package com.hegetomi.orderlib.dto;

import lombok.Data;

@Data
public class OrderedProductDto {
    private Long id;
    private String productName;
    private Long unitPrice;
    private Long quantity;
}
