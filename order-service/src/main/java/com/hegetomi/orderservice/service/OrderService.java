package com.hegetomi.orderservice.service;

import com.hegetomi.externalshipping.wsclient.ShippingXmlWs;
import com.hegetomi.externalshipping.wsclient.ShippingXmlWsImplService;
import com.hegetomi.orderservice.enums.Status;
import com.hegetomi.orderservice.mapper.AppOrderMapper;
import com.hegetomi.orderservice.model.AppOrder;
import com.hegetomi.orderservice.repository.AppOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final AppOrderRepository orderRepository;
    private final AppOrderMapper mapper;

    @Value("${com.hegetomi.orderservice.pickup}")
    private String location;

    @Transactional
    public AppOrder save(AppOrder order) {
        order.setStatus(Status.PENDING);
        return orderRepository.save(order);
    }

    public List<AppOrder> findByUsername(String username) {
        List<AppOrder> allByUsername = orderRepository.findAllByUsername(username);
        List<Long> ids = allByUsername.stream().map(AppOrder::getId).toList();
        allByUsername = orderRepository.findAllWithProducts(ids);
        return allByUsername;
    }

    @Transactional
    public Optional<AppOrder> updateStatus(Long id, Status status) {
        Optional<AppOrder> appOrderOptional = orderRepository.findById(id);
        if (appOrderOptional.isPresent()) {
            AppOrder appOrder = appOrderOptional.get();
            appOrder.setStatus(status);
            if (status.equals(Status.CONFIRMED)) {
                sendToShippingProvider(appOrder);
            }
            return Optional.of(appOrder);
        }
        return Optional.empty();
    }

    @Transactional
    protected void sendToShippingProvider(AppOrder appOrder) {
        log.warn("WSDL USED");
        ShippingXmlWs shippingXmlWs = new ShippingXmlWsImplService().getShippingXmlWsImplPort();
        Long shipmentId = shippingXmlWs.receiveDeliverable(mapper.modelToWsDto(appOrder), location);
        log.warn(shipmentId + "");
        appOrder.setShipmentId(shipmentId);

    }
}
