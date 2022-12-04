package com.hegetomi.orderservice.mapper;

import com.hegetomi.orderservice.dto.AppOrderDto;
import com.hegetomi.orderservice.dto.OrderedProductDto;
import com.hegetomi.orderservice.enums.Status;
import com.hegetomi.orderservice.model.AppOrder;
import com.hegetomi.orderservice.model.OrderedProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface AppOrderMapper {

    AppOrder dtoToModel(AppOrderDto dto);

    AppOrderDto modelToDto(AppOrder order);

    @Mapping(target = "orderedProductList", ignore = true)
    AppOrderDto modelToDtoNoProducts(AppOrder order);

    @Mapping(target = "appOrder", ignore = true)
    OrderedProductDto productModelToDto(OrderedProduct orderedProduct);

    com.hegetomi.externalshipping.wsclient.AppOrderDto modelToWsDto(AppOrder appOrder);

}
