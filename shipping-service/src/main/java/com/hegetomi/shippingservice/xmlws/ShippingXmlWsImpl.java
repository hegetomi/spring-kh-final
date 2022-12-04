package com.hegetomi.shippingservice.xmlws;

import com.hegetomi.orderlib.dto.AppOrderDto;
import com.hegetomi.shippingservice.jmsservice.ShipmentStatusService;
import enums.ShippingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingXmlWsImpl implements ShippingXmlWs {
    private final Random random = new Random();
    private final ShipmentStatusService service;


    @Override
    public Long receiveDeliverable(AppOrderDto orderDto, String pickupLocation) {
        Long shippingId = random.nextLong(0, 9999);
        service.processOrder(orderDto, pickupLocation, shippingId);
        return shippingId;
    }


}

