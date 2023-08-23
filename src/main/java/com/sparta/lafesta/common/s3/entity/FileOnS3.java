package com.sparta.lafesta.common.s3.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileOnS3 {

    /////컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.


    private String originalFileName;
    private String uploadFileUrl;
//    private String uploadFileName;
//    private String uploadFilePath;

    ////생성자 - 약속된 형태로만 생성가능하도록 합니다.



    ////연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.



    ////연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.



    //// 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)

}
