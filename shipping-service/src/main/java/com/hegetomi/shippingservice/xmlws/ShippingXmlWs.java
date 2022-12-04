package com.hegetomi.shippingservice.xmlws;


import com.hegetomi.orderlib.dto.AppOrderDto;


import javax.jws.WebService;
import java.util.concurrent.CompletableFuture;

@WebService
public interface ShippingXmlWs {

    Long receiveDeliverable(AppOrderDto orderDto, String pickupLocation);

}
