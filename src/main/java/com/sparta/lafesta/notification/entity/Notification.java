package com.sparta.lafesta.notification.entity;

import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "rd", nullable = false)
    private Boolean rd;

    // 알림 받을 follower
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User follower;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    public Notification(String title, String detail, LocalDateTime createdAt, String destination, User follower) {
        this.follower = follower;
        this.title = title;
        this.detail = detail;
        this.createdAt = createdAt;
        this.destination = destination;
        this.rd = false;
        this.expirationTime = LocalDateTime.now().plusDays(7);
    }

    public void readNotification() {
        this.rd = true;
        this.expirationTime = LocalDateTime.now().plusDays(3);
    }
}
