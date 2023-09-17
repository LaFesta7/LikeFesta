package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.entity.Tag;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TagService {

    /**
     * 전체 태그 조회
     *
     * @param user 권한 확인을 위한 유저 정보
     * @param pageable 페이징을 위한 정보
     * @return 전체 태그 조회 결과
     */
    Page<TagResponseDto> selectTags(User user, Pageable pageable);

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
     * @param pageable 페이징 정보
     * @return 태그별 페스티벌 조회 결과
     */
    Page<FestivalResponseDto> selectFestivalTags(String tag, User user, Pageable pageable);

    /**
     * 페스티벌 태그 삭제
     *
     * @param festivalId 태그를 삭제할 페스티벌의 id
     * @param tagId      페스티벌에서 제거될 태그의 id
     * @param user       권한 확인을 위한 유저 정보
     * @return 태그 삭제 결과
     */
    ResponseEntity<ApiResponseDto> deleteFestivalTag(Long festivalId, Long tagId, User user);

    /**
     * 존재하는 태그인지 확인 -> 없으면 생성 / 존재하는 태그면 가져오기
     *
     * @param requestDto 태그 생성 요청 정보
     * @return 태그 반환 결과
     */
    Tag checkTag(TagRequestDto requestDto);

    /**
     * 태그 타이틀로 태그 가져오기
     *
     * @param title 태그 타이틀
     * @return 태그 반환 결과
     */
    Tag findTagByTitle(String title);
}
