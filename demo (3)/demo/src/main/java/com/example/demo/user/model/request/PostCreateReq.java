package com.example.demo.user.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Builder
@Data
public class PostCreateReq {
    private String email;
    private String password;
    private String name;
    private MultipartFile image;
}
