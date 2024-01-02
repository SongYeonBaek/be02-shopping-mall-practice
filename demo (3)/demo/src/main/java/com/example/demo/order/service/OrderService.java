package com.example.demo.order.service;

import com.example.demo.product.model.Product;
import com.example.demo.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Integer validate(int id){
         Optional<Product> product = productRepository.findById(id);
         if(product.isPresent()){
             return product.get().getPrice();
         }
         else {
             return null;
         }
    }

}
