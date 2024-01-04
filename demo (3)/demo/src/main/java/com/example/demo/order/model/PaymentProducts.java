package com.example.demo.order.model;

import com.example.demo.product.model.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentProducts {
    List<Product> products;
}
