package com.sparta.lafesta.common.s3;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class S3TestController {

    private final S3UploadService s3UploadService;

    @PostMapping("/s3-test/upload")
    public ResponseEntity<ApiResponseDto> uploadFile(
            @Valid @RequestPart TestRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        List<String> fileUrlList = new ArrayList<>();

        if (files != null) {
            fileUrlList = s3UploadService.uploadFiles(files);
        }

        log.info(requestDto.toString());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), fileUrlList.toString()));
    }
}
