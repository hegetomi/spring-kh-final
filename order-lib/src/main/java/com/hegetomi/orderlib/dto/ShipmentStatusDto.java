package com.hegetomi.orderlib.dto;

import enums.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentStatusDto {
    private Long id;
    private ShippingStatus status;
}
