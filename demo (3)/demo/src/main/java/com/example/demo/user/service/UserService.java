package com.example.demo.user.service;

/*
@Service
public class UserService {
    private UserRepository userRepository;

    @Value("${project.upload.path}")
    private String uploadPath;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private AmazonS3 s3;

    public UserService(UserRepository userRepository, AmazonS3 s3) {
        this.userRepository = userRepository;
        this.s3 = s3;
    }

    public String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
//        folderPath = uploadPath + File.separator + folderPath;

        File uploadPathFolder = new File(uploadPath, folderPath);
        if(uploadPathFolder.exists() == false) {
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

        try {
//            file.transferTo(saveFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3.putObject(bucket, saveFileName.replace(File.separator, "/"), file.getInputStream(), metadata);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return s3.getUrl(bucket, saveFileName.replace(File.separator, "/")).toString();    }

    public PostCreateUserRes create(PostCreateReq postCreateReq){
        String saveFilename = saveFile(postCreateReq.getImage());

        User user = User.builder()
                .email(postCreateReq.getEmail())
                .password(postCreateReq.getPassword())
                .name(postCreateReq.getName())
                .image(saveFilename.replace(File.separator, "/"))
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

    public void read(Long id){
        userRepository.findById(id);
    }
}
*/

