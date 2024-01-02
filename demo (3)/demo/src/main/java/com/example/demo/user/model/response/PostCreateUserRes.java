package com.example.demo.user.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class PostCreateUserRes {
    private Long id;
    private String email;
    private String name;
    private String image;

}
