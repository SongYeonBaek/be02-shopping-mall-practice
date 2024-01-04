package com.example.demo.product.service;

import com.example.demo.order.model.PaymentProducts;
import com.example.demo.product.model.Product;
import com.example.demo.product.model.ProductDto;
import com.example.demo.product.repository.ProductRepository;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {
    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> list(){

        List<Product> result = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for(Product product : result){
            productDtos.add(ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build());
        }

        return productDtos;
    }

    public Integer getTotalPrice(PaymentProducts datas){

        List<Integer> productIds = new ArrayList<>();
        for (Product product: datas.getProducts()) {
            productIds.add(product.getId());
        }

        List<Product> products = productRepository.findAllById(productIds);

        Integer totalPrice = 0;
        for (Product product: products) {
            totalPrice += product.getPrice();
        }

        return  totalPrice;
    }
}
