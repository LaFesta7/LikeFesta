package com.sparta.lafesta.user.entity;

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
@EqualsAndHashCode(of="id")
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

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserFollow> followings = new ArrayList<>(); //내가 팔로우 하는 사람들

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserFollow> followers = new ArrayList<>();  //나를 팔로우 하는 사람들

    @OneToMany(mappedBy = "followingFestivalUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FestivalFollow> followedFestivals = new ArrayList<>();

    public User(String username, String password, String email, UserRoleEnum role, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
    }
}