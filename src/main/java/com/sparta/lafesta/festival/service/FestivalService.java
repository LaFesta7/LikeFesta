package com.sparta.lafesta.festival.service;

import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.notification.dto.ReminderDto;
import com.sparta.lafesta.user.entity.User;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface FestivalService {

    /**
     * 페스티벌 생성
     *
     * @param requestDto 생성할 페스티벌의 정보
     * @param files      생성할 첨부파일 정보
     * @param user       권한 확인
     * @return 페스티벌 추가 결과
     */
    FestivalResponseDto createFestival(FestivalRequestDto requestDto,
                                       List<MultipartFile> files, User user) throws IOException;
//  FestivalResponseDto createFestival(FestivalRequestDto requestDto, User user) throws IOException;

    /**
     * 전체 페스티벌 조회
     *
     * @param pageable 페이징 처리를 위한 정보
     * @return 전체 페스티벌 조회 결과
     */
    List<FestivalResponseDto> selectFestivals(Pageable pageable);

    /**
     * 페스티벌 상세 조회
     *
     * @param festivalId 조회할 페스티벌의 id
     * @param user       권한 확인
     * @return 페스티벌 상세 조회 결과
     */
    FestivalResponseDto selectFestival(Long festivalId, User user);

    /**
     * 페스티벌 수정
     *
     * @param festivalId 수정할 페스티벌의 id
     * @param requestDto 수정할 정보
     * @param files      수정할 첨부파일 정보
     * @param user       권한 확인
     * @return 페스티벌 수정 결과
     */
    FestivalResponseDto modifyFestival(Long festivalId, FestivalRequestDto requestDto,
                                       List<MultipartFile> files, User user) throws IOException;

    /**
     * 페스티벌 삭제
     *
     * @param festivalId 삭제할 페스티벌의 id
     * @param user       권한 확인
     */
    void deleteFestival(Long festivalId, User user);

    /**
     * 선택한 페스티벌 좋아요 추가
     *
     * @param festivalId 좋아요 추가할 페스티벌의 id
     * @param user       권한 확인
     * @return 좋아요 추가 결과
     */
    FestivalResponseDto createFestivalLike(Long festivalId, User user);

    /**
     * 선택한 페스티벌 좋아요 취소
     *
     * @param festivalId 좋아요 취소할 페스티벌의 id
     * @param user       권한 확인
     * @return 좋아요 취소 결과
     */
    FestivalResponseDto deleteFestivalLike(Long festivalId, User user);

    /**
     * 페스티벌 랭킹 조회
     *
     * @param user 권한 확인
     * @return 페스티벌 랭킹 조회 결과
     */
    List<FestivalResponseDto> selectFestivalRanking(User user);

    /**
     * 페스티벌 오픈 알림을 보낼 페스티벌 가져오기
     *
     * @return 페스티벌 오픈 알림을 보낼 페스티벌 가져오기 결과
     */
    List<ReminderDto> getFestivalOpenReminders();

    /**
     * 페스티벌 예매 오픈 알림을 보낼 페스티벌 가져오기
     *
     * @return 페스티벌 예매 오픈 알림을 보낼 페스티벌 가져오기 결과
     */
    List<ReminderDto> getReservationOpenReminders();

    /**
     * 페스티벌 리뷰 독려 알림을 보낼 페스티벌 가져오기
     *
     * @return 페스티벌 리뷰 독려 알림을 보낼 페스티벌 가져오기 결과
     */
    List<ReminderDto> getReviewEncouragementReminders();

    /**
     * id로 페스티벌 가져오기
     *
     * @param festivalId 가져올 페스티벌의 id
     * @return 가져온 페스티벌
     */
    Festival findFestival(Long festivalId);
    
}
