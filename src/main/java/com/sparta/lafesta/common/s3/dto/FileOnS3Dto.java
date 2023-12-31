package com.sparta.lafesta.common.s3.dto;

import com.sparta.lafesta.common.s3.entity.*;
import lombok.Getter;

@Getter
public class FileOnS3Dto {

    private Long id;
    private String keyName;
    private String uploadFileUrl;


    public FileOnS3Dto(FestivalFileOnS3 fileOnS3) {
        this.id = fileOnS3.getId();
        this.keyName = fileOnS3.getKeyName();
        this.uploadFileUrl = fileOnS3.getUploadFileUrl();
    }

    public FileOnS3Dto(ReviewFileOnS3 fileOnS3) {
        this.id = fileOnS3.getId();
        this.keyName = fileOnS3.getKeyName();
        this.uploadFileUrl = fileOnS3.getUploadFileUrl();
    }

    public FileOnS3Dto(UserFileOnS3 fileOnS3) {
        this.id = fileOnS3.getId();
        this.keyName = fileOnS3.getKeyName();
        this.uploadFileUrl = fileOnS3.getUploadFileUrl();
    }

    public FileOnS3Dto(BadgeFileOnS3 fileOnS3) {
        this.id = fileOnS3.getId();
        this.keyName = fileOnS3.getKeyName();
        this.uploadFileUrl = fileOnS3.getUploadFileUrl();
    }

}
