package com.hegetomi.catalogservice.model;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Category {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private Long id;
    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private List<Product> products;
}
