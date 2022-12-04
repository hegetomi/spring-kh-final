package com.hegetomi.orderservice.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class OrderedProduct {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;

    private String productName;
    private String categoryName;
    private Long unitPrice;
    private Long quantity;

    @ManyToOne
    private AppOrder appOrder;
}
