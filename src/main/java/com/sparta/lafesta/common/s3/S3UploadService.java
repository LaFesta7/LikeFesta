package com.sparta.lafesta.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

    // S3 연동하기 위한 주입받아오기
    private final AmazonS3 amazonS3;

    //버킷이름 받아오기
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<FileOnS3> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<FileOnS3> FileOnS3s = new ArrayList<>();

        //모든 파일들 반복적으로 업로드
        for (MultipartFile multipartFile : multipartFiles) {
            // 업로드하고 FestivalFileOnS3 객체로 받음
            FileOnS3 fileOnS3 = uploadSingleFile(multipartFile);
            //받은 객체 List에 추가
            FileOnS3s.add(fileOnS3);
        }

        return FileOnS3s;
    }


    public String deleteFile(String keyName) {

        try {
            boolean isObjectExist = amazonS3.doesObjectExist(bucket, keyName);
            if (isObjectExist) {
                amazonS3.deleteObject(bucket, keyName);
            } else {
                return "file not found";
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }
        return "file delete Success";
    }


    private FileOnS3 uploadSingleFile(MultipartFile multipartFile) throws IOException {
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

        String uploadedFileUrl = amazonS3.getUrl(bucket, originalFilename).toString();

        return new FileOnS3(originalFilename, uploadedFileUrl);
    }

    private String generateFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", "/");
    }

}
