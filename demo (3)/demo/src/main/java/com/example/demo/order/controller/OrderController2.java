package com.example.demo.order.controller;

import com.example.demo.order.service.OrderService2;
import com.example.demo.product.service.ProductService;
import com.example.demo.user.model.request.PostCreateUserReq;
import com.example.demo.user.model.response.PostCreateUserRes;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController2 {
    private OrderService2 orderService2;
    private ProductService productService;
    private JavaMailSender emailSender;
    private UserService userService;

    @RequestMapping("/validation")
    public ResponseEntity validation(String impUid){
        try{
            if(orderService2.paymentValidation(impUid)){

                //order 데이터 저장, orderedProduct 데이터 저장
                orderService2.createOrder(impUid);

                return ResponseEntity.ok().body("ok");
            }
            return ResponseEntity.ok().body("error");
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET,value = "/list")
    public ResponseEntity list(){
        return ResponseEntity.ok().body(orderService2.list());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity create(PostCreateUserReq request){
        PostCreateUserRes response = userService.create(request);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("test");
        message.setText("msg");

        emailSender.send(message);
        return ResponseEntity.ok().body(response);
    }
}
