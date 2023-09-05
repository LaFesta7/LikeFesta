package com.sparta.lafesta.user.entity;

import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import com.sparta.lafesta.common.s3.entity.UserFileOnS3;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.follow.entity.UserFollow;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "users")
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String introduce;

    @Column
    private Boolean organizerRequest;



    @Column(nullable = true, unique = true)
    private Long kakaoId;

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserFollow> followings = new ArrayList<>(); //내가 팔로우 하는 사람들

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserFollow> followers = new ArrayList<>();  //나를 팔로우 하는 사람들

    @OneToMany(mappedBy = "followingFestivalUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FestivalFollow> followedFestivals = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true) //todo 이후 프론트 전달 방식과 관련해서 개선필요해 보임
    private List<UserFileOnS3> userFileOnS3s = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

    public User(String username, String password, String email, UserRoleEnum role, String nickname, Boolean organizerRequest) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.organizerRequest = organizerRequest;
    }

    public User(String username, String password, String email, UserRoleEnum role, String nickname, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
    }

    public void approveOrganizer() {
        this.role = UserRoleEnum.ORGANIZER;
        this.organizerRequest = false;
    }

    public User kakaoIdUpdate(Long kakaoId) { // kakaoId를 받아서 업데이트
        this.kakaoId = kakaoId;
        return this;
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }

    public void modifyIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void modifyEmail(String email) {
        this.email = email;
    }

    public void modifyPassword(String password) {
        this.password = password;
    }
}