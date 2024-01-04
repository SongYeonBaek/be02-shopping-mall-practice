package com.example.demo.order.model;

import com.example.demo.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String impUid;

    @ManyToOne
    @JoinColumn(name = "User_id")
    User user;

    @OneToMany(mappedBy = "orders")
    private List<OrderedProduct> orderProductsList = new ArrayList<>();
}
