package com.example.demo.order.model;

import com.example.demo.product.model.Product;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="Product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name="Orders_id")
    Orders orders;
}
