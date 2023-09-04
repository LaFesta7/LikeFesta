package com.sparta.lafesta.follow.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.follow.entity.UserFollow;
import com.sparta.lafesta.follow.repository.FestivalFollowRepository;
import com.sparta.lafesta.follow.repository.UserFollowRepository;
import com.sparta.lafesta.user.dto.SelectUserResponseDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;
    private final FestivalFollowRepository festivalFollowRepository;
    private final FestivalRepository festivalRepository;


    //유저 팔로우 추가
    @Transactional
    public ResponseEntity<ApiResponseDto> followingUser(UserDetailsImpl userDetails, Long followedUserId){
        //토큰 체크
        User follower = userDetails.getUser();

        if(follower == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        //팔로우할 유저 조회
        User followedUser = userRepository.findById(followedUserId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        //본인 팔로우 불가능
        if(follower.getId().equals(followedUser.getId())){
            throw new IllegalArgumentException("자신을 팔로우 할 수 없습니다.");
        }

        //중복 팔로우 예외 발생
        if(userFollowRepository.findByFollowedUserAndFollowingUser(followedUser, follower).isPresent()){
            throw new IllegalArgumentException("이미 팔로우 한 유저입니다.");
        }

        userFollowRepository.save(new UserFollow(followedUser, follower));

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "팔로우 성공. 유저이름: " + followedUser.getUsername()));
    }

    //유저 팔로워 목록 조회 - 나를 팔로우 하는 유저
    @Transactional(readOnly = true)
    public List<SelectUserResponseDto> selectFollowers(UserDetailsImpl userDetails, Pageable pageable){
        User followedUser = userDetails.getUser();

        if(followedUser == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        List<UserFollow> followers = userFollowRepository.findAllByFollowedUser(followedUser, pageable);

        List<SelectUserResponseDto> followerUsers = new ArrayList<>();
        for(UserFollow follower : followers){
            User followerUser = userRepository.findByFollowers(follower)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

            SelectUserResponseDto selectUserResponseDto = new SelectUserResponseDto(followerUser);
            followerUsers.add(selectUserResponseDto);
        }

        return followerUsers;
    }

//    유저 팔로잉 목록 조회 - 내가 팔로우 하는 유저
    @Transactional(readOnly = true)
    public List<SelectUserResponseDto> selectFollowingUsers(UserDetailsImpl userDetails, Pageable pageable){
        User follower = userDetails.getUser();

        if(follower == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        List<UserFollow> followings = userFollowRepository.findAllByFollowingUser(follower, pageable);

        List<SelectUserResponseDto> followingUsers = new ArrayList<>();
        for(UserFollow following : followings){
            User followingUser = userRepository.findByFollowings(following)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

            SelectUserResponseDto selectUserResponseDto = new SelectUserResponseDto(followingUser);
            followingUsers.add(selectUserResponseDto);
        }

        return followingUsers;
    }

    //유저 팔로우 취소
    @Transactional
    public ResponseEntity<ApiResponseDto> unfollowingUser(UserDetailsImpl userDetails, Long followedUserId){
        User follower = userDetails.getUser();

        if(follower == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        //언팔로우 할 유저 조회
        User followedUser = userRepository.findById(followedUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        //팔로우 관계 확인
        UserFollow userFollow = userFollowRepository.findByFollowedUserAndFollowingUser(followedUser, follower)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 아닙니다."));

        userFollowRepository.delete(userFollow);

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), followedUser.getUsername() + "님을 언팔로우 했습니다."));
    }


    //페스티벌 팔로우
    @Transactional
    public ResponseEntity<ApiResponseDto> followingFestival(UserDetailsImpl userDetails, Long festivalId){
        //토큰 체크
        User follower = userDetails.getUser();

        if(follower == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        //팔로우할 페스티벌 조회
        Festival followedFestival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 페스티벌이 없습니다."));


        //중복 팔로우 예외 발생
        if(festivalFollowRepository.findByFollowedFestivalAndFollowingFestivalUser(followedFestival, follower).isPresent()){
            throw new IllegalArgumentException("이미 팔로우 한 페스티벌입니다.");
        }

        festivalFollowRepository.save(new FestivalFollow(followedFestival, follower));

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "팔로우 성공. 페스티벌 이름: " + followedFestival.getTitle()));
    }

    //페스티벌 팔로우 목록 조회
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> selectFollowingFestivals(UserDetailsImpl userDetails, Pageable pageable){
        User followingUser = userDetails.getUser();

        if(followingUser == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        List<FestivalFollow> follows = festivalFollowRepository.findAllByFollowingFestivalUser(followingUser, pageable);

        List<FestivalResponseDto> followedFestivals = new ArrayList<>();
        for(FestivalFollow festivalFollow : follows){
            Festival followedFestival = festivalRepository.findByFestivalFollowers(festivalFollow)
                    .orElseThrow(() -> new IllegalArgumentException("해당 페스티벌이 없습니다."));

            FestivalResponseDto festivalResponseDto = new FestivalResponseDto(followedFestival);
            followedFestivals.add(festivalResponseDto);
        }
        return followedFestivals;
    }

    //페스티벌 팔로우 취소
    @Transactional
    public ResponseEntity<ApiResponseDto> unfollowingFestival(UserDetailsImpl userDetails, Long festivalId){
        User follower = userDetails.getUser();

        if(follower == null){
            throw new IllegalArgumentException("로그인 해주세요");
        }

        //언팔로우 할 페스티벌 조회
        Festival followedFestival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 페스티벌이 없습니다."));

        //팔로우 관계 확인
        FestivalFollow festivalFollow = festivalFollowRepository.findByFollowedFestivalAndFollowingFestivalUser(followedFestival, follower)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 아닙니다."));

        festivalFollowRepository.delete(festivalFollow);

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), followedFestival.getTitle() + "을(를) 언팔로우 했습니다."));
    }

    // 팔로우 유저 찾기
    public List<User> findFollowers(User followedUser) {
        List<UserFollow> followUsers = userFollowRepository.findAllByFollowedUser(followedUser);
        List<User> followers = new ArrayList<>();
        for (UserFollow follower : followUsers) {
            User followerUser = userRepository.findByFollowers(follower).orElse(null);
            followers.add(followerUser);
        }
        return followers;
    }
}
