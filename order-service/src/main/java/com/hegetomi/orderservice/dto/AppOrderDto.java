package com.hegetomi.orderservice.dto;

import com.hegetomi.orderservice.enums.Status;
import lombok.Data;

import java.util.List;

@Data
public class AppOrderDto {
    private Long id;

    private String username;
    private String shippingAddress;
    private Status status;
    private Long shipmentId;
    private List<OrderedProductDto> orderedProductList = new java.util.ArrayList<>();
}
