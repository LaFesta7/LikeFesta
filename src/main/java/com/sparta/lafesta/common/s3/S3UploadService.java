package com.sparta.lafesta.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    // S3 연동하기 위한 주입받아오기
    private final AmazonS3 amazonS3;

    //버킷이름 받아오기
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uploadFile(MultipartFile multipartFile) throws IOException {

        //파일 이름 가져오기.
        String originalFilename = multipartFile.getOriginalFilename();

        //멀티파트파일 -> 인풋스트림으로 변환
        InputStream inputStreamFile = multipartFile.getInputStream();

        //메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());


        //putObject 메소드 실행
        amazonS3.putObject(bucket, originalFilename, inputStreamFile, metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }
}
