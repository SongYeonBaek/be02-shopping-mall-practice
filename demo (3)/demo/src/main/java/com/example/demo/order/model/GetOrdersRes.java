package com.example.demo.order.model;

import com.example.demo.product.model.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class GetOrdersRes {
    Long id;
    String userName;
    List<ProductDto> products;
}
