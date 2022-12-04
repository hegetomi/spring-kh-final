package com.hegetomi.orderservice.model;

import com.hegetomi.orderservice.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AppOrder {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;

    private String username;
    private String shippingAddress;
    private Status status;
    private Long shipmentId;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "app_order_id")
    private List<OrderedProduct> orderedProductList = new java.util.ArrayList<>();


}
