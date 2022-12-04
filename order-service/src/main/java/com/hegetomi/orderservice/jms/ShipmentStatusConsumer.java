package com.hegetomi.orderservice.jms;

import com.hegetomi.orderlib.dto.ShipmentStatusDto;
import com.hegetomi.orderservice.dto.AppOrderDto;
import com.hegetomi.orderservice.enums.Status;
import com.hegetomi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentStatusConsumer {

    private final OrderService orderService;

    @JmsListener(destination = "/shipment")
    public void onShipmentMessage(ShipmentStatusDto shipmentStatusDto) {
        Status status = Status.valueOf(shipmentStatusDto.getStatus().name());
        log.warn("REQUEST TO MQ RECEIVED");
        orderService.updateStatus(shipmentStatusDto.getId(), status);
    }

}
