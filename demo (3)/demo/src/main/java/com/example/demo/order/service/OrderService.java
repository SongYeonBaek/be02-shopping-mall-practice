//package com.example.demo.order.service;
//
//import com.example.demo.product.model.Product;
//import com.example.demo.product.repository.ProductRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class OrderService {
//    ProductRepository productRepository;
//
//    public OrderService(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    public Integer validate(int id){
//         Optional<Product> product = productRepository.findById(id);
//         if(product.isPresent()) return product.get().getPrice();
//         else return null;
//    }
//
//    public Map<String, String> parsing(String response){
//        String amount = response.split("amount")[1].split(",")[0].replace("=", "");
//        String name = response.split(" name")[1].split(",")[0].replace("=", "");
//        String custom_data = response.split(" custom_data=")[1].split("]")[0];
//
//        int count = 0;
//        for (int i = 0; i < custom_data.length(); i++) {
//            if(custom_data.charAt(i) == '{') count++;
//        }
//        System.out.println(custom_data);
//        System.out.println("count: " +count);
//
//        Integer totalAmount = 0;
//        for (int i = 1; i <= count; i++) {
//            String id = custom_data.split("id")[i].split(":")[1].split(",")[0];
//            String price = custom_data.split("price")[i].split(":")[1].split("}")[0];
//            System.out.println("id: " + id + " , price: " + price);
//            totalAmount += validate(Integer.valueOf(id));
//        }
//        System.out.println(totalAmount);
//
//        Map<String, String> result = new HashMap<>();
//        result.put("name", name);
//        result.put("amount", amount);
//        result.put("correctPrice", Integer.toString(totalAmount));
//
//        return result;
//    }
//
//}
