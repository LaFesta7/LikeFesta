package com.sparta.lafesta.common.s3.entity;

import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity(name = "user_file_on_s3")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFileOnS3 extends FileOnS3{
    ////컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    ////생성자 - 약속된 형태로만 생성가능하도록 합니다.

    public UserFileOnS3(FileOnS3 fileOnS3){
        super(fileOnS3.getKeyName(), fileOnS3.getUploadFileUrl());
//        this.originalFileName = fileOnS3.getOriginalFileName();
//        this.uploadFileUrl = fileOnS3.getUploadFileUrl();
    }

    ////연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    ////연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.

    //// 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
}
