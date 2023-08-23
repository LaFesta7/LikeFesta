package com.sparta.lafesta.common.s3.dto;

import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import lombok.Getter;

@Getter
public class FileOnS3Dto {

    private Long id;
    private String originalFileName;
    private String uploadFileUrl;


    public FileOnS3Dto(FestivalFileOnS3 fileOnS3) {
        this.id = fileOnS3.getId();
        this.originalFileName = fileOnS3.getOriginalFileName();
        this.uploadFileUrl = fileOnS3.getUploadFileUrl();
    }

}
