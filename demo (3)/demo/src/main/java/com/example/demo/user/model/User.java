package com.example.demo.user.model;

import com.example.demo.order.model.Orders;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 200)
    private String password;
    @Column(length = 30)
    private String name;
    @Column(length = 200, unique = true)
    private String image;

    private Boolean isValid;

    @OneToMany(mappedBy = "user")
    private List<Orders> orderList = new ArrayList<>();

}
