package com.example.demo.user.model.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class PostCreateUserReq {
    private String email;
    private String password;
    private String name;
    private MultipartFile image;
}
