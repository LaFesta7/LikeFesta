package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface TagService {

    /**
     * 전체 태그 조회
     *
     * @param user 권한 확인을 위한 유저 정보
     * @return 전체 태그 조회 결과
     */
    List<TagResponseDto> selectTags(User user);

    /**
     * 태그 수정
     *
     * @param tagId      수정할 태그의 id
     * @param requestDto 수정할 태그의 정보
     * @param user       권한 확인을 위한 유저 정보
     * @return 태그 수정 결과
     */
    TagResponseDto modifyTag(Long tagId, TagRequestDto requestDto, User user);

    /**
     * 태그 삭제
     *
     * @param tagId 삭제할 태그의 id
     * @param user  권한 확인을 위한 유저 정보
     */
    void deleteTag(Long tagId, User user);

    /**
     * 페스티벌 태그별 조회
     *
     * @param tag  조회할 태그의 정보
     * @param user 권한 확인을 위한 유저 정보
     * @return 태그별 페스티벌 조회 결과
     */
    List<FestivalResponseDto> selectFestivalTags(String tag, User user);

    /**
     * 페스티벌 태그 삭제
     *
     * @param festivalId 태그를 삭제할 페스티벌의 id
     * @param tagId      페스티벌에서 제거될 태그의 id
     * @param user       권한 확인을 위한 유저 정보
     * @return 태그 삭제 결과
     */
    ResponseEntity<ApiResponseDto> deleteFestivalTag(Long festivalId, Long tagId, User user);
}
