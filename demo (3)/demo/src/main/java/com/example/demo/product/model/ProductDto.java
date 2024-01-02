package com.example.demo.product.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ProductDto {
    Integer id;
    String name;
    Integer price;

//    List<OrdersDto> orders = new ArrayList<>();
}
