package com.sparta.lafesta.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.results.graph.tuple.TupleResult;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "users")
@EqualsAndHashCode(of="id")
public class User  {

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

    @Column(nullable = true, unique = true)
    private Long kakaoId;

    public User(String username, String password, String email, UserRoleEnum role, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
    }

    public User(String username, String password, String email, UserRoleEnum role, String nickname, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.kakaoId =kakaoId;
    }
    public User kakaoIdUpdate(Long kakaoId) { // kakaoId를 받아서 업데이트
        this.kakaoId = kakaoId;
        return this;
    }
}