package com.hegetomi.orderlib.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppOrderDto {
    private Long id;

    private String username;
    private String shippingAddress;
    private List<OrderedProductDto> orderedProductList = new ArrayList<>();

}
