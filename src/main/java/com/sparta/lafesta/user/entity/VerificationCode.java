package com.sparta.lafesta.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "verification_code")
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @Column
    private Boolean confirm = false;

    public VerificationCode (String email, String code) {
        this.email = email;
        this.code = code;
        this.expirationTime = LocalDateTime.now().plusMinutes(3);
    }

    public void verificationCodeConfirm() {
        this.confirm = true;
    }
}