package com.example.demo.user.controller;

import com.example.demo.user.model.request.PostCreateUserReq;
import com.example.demo.user.model.response.PostCreateUserRes;
import com.example.demo.user.service.EmailVerifyService;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JavaMailSender emailSender;
    private final EmailVerifyService emailVerifyService;

    @RequestMapping(method = RequestMethod.GET, value = "/verify")
    public ResponseEntity verify(String email, String uuid) {
        if(emailVerifyService.verify(email, uuid)) {
            userService.update(email);
            return ResponseEntity.ok().body("ok");
        }
        return ResponseEntity.ok().body("error");
    }


    //User 회원가입 (isValid=false가 default)
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity create(PostCreateUserReq request){
        PostCreateUserRes response = userService.create(request);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("[백마켓] 이메일 인증");

        String uuid = UUID.randomUUID().toString();
        message.setText("http://localhost:8080/user/verify?email="+request.getEmail()+"&uuid="+uuid);
        emailSender.send(message);

        emailVerifyService.create(request.getEmail(), uuid);

        return ResponseEntity.ok().body(response);
    }

}

