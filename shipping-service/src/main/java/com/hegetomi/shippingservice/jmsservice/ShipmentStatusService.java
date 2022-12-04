package com.hegetomi.shippingservice.jmsservice;

import com.hegetomi.orderlib.dto.AppOrderDto;
import com.hegetomi.orderlib.dto.ShipmentStatusDto;
import enums.ShippingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class ShipmentStatusService {
    private final Random random = new Random();
    private final JmsTemplate jmsTemplate;

    public void handlePayment(Long studentId, ShippingStatus status){
        ShipmentStatusDto payload = new ShipmentStatusDto(studentId, status);
        jmsTemplate.convertAndSend("/shipment", payload);
    }

    @Async
    public void processOrder(AppOrderDto orderDto, String pickupLocation, Long shippingId) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (random.nextLong() % 5 > 1) {
            handlePayment(orderDto.getId(), ShippingStatus.DELIVERED);
        } else {
            handlePayment(orderDto.getId(), ShippingStatus.SHIPMENT_FAILED);
        }
    }
}
