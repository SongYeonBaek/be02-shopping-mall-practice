package com.example.demo.user.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.user.model.User;
import com.example.demo.user.model.request.PostCreateUserReq;
import com.example.demo.user.model.response.PostCreateUserRes;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Value("${project.upload.path}")
    private String uploadPath;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private AmazonS3 s3;


    public String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath, folderPath);
        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    public String saveFile(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String folderPath = makeFolder();
        String uuid = UUID.randomUUID().toString();
        String saveFileName = folderPath + File.separator + uuid + "_" + originalName;
//        File saveFile = new File(uploadPath, saveFileName);
        InputStream input = null;
        try {
//            file.transferTo(saveFile);
            input = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());


            s3.putObject(bucket, saveFileName.replace(File.separator, "/"), input, metadata);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return s3.getUrl(bucket, saveFileName.replace(File.separator, "/")).toString();
    }


    public PostCreateUserRes create(PostCreateUserReq postCreateUserReq) {
        String saveFileName = null;
        if(postCreateUserReq.getImage() != null) {
            saveFileName = saveFile(postCreateUserReq.getImage());
            saveFileName.replace(File.separator, "/");
        }

        User user = User.builder()
                .email(postCreateUserReq.getEmail())
                .password(postCreateUserReq.getPassword())
                .name(postCreateUserReq.getName())
                .image(saveFileName)
                .isValid(false)
                .build();

        User result = userRepository.save(user);

        PostCreateUserRes response = PostCreateUserRes.builder()
                .id(result.getId())
                .email(result.getEmail())
                .name(result.getName())
                .image(result.getImage())
                .build();

        return response;
    }

    public void read(Long id) {
        userRepository.findById(id);
    }

    public void update(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        if(result.isPresent()) {
            User user = result.get();
            user.setIsValid(true);
            userRepository.save(user);
        }
    }
}


