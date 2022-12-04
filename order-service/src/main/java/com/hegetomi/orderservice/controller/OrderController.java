package com.hegetomi.orderservice.controller;

import com.hegetomi.orderservice.dto.AppOrderDto;
import com.hegetomi.orderservice.enums.AllowedStatus;
import com.hegetomi.orderservice.enums.Status;
import com.hegetomi.orderservice.mapper.AppOrderMapper;
import com.hegetomi.orderservice.model.AppOrder;
import com.hegetomi.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final AppOrderMapper orderMapper;


    @PostMapping
    public AppOrderDto postNew(@RequestBody AppOrderDto orderDto) {
        orderDto.setId(null);
        orderDto.setShipmentId(null);
        AppOrder appOrder = orderMapper.dtoToModel(orderDto);
        log.warn(appOrder.getOrderedProductList().size() + "");
        return orderMapper.modelToDto(orderService.save(appOrder));
    }

    @GetMapping
    public List<AppOrderDto> getForUsername(@RequestParam(name = "name") String username) {
        return orderService.findByUsername(username).stream().map(orderMapper::modelToDto).toList();
    }

    @PutMapping("/{id}")
    public AppOrderDto updateStatus(@PathVariable Long id, @RequestParam(name = "status") @AllowedStatus(anyOf = {Status.DECLINED, Status.CONFIRMED}) Status status) {
        Optional<AppOrder> appOrderOptional = orderService.updateStatus(id, status);
        return orderMapper.modelToDtoNoProducts(appOrderOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
