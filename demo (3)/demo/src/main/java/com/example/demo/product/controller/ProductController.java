package com.example.demo.product.controller;

import com.example.demo.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity list(){

        return ResponseEntity.ok().body(productService.list());
    }
}
