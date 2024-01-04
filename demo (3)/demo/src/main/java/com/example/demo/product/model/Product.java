package com.example.demo.product.model;
import com.example.demo.order.model.OrderedProduct;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    Integer price;

    @OneToMany(mappedBy = "product")
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

}
