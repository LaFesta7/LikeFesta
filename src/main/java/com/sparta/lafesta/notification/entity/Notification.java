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

    @Column(name = "editor", nullable = false)
    private String editor;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "`read`", nullable = false)
    private Boolean read;

    // 알림 받을 follower
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User follower;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    public Notification(String title, String editor, LocalDateTime createdAt, User follower) {
        this.follower = follower;
        this.title = title;
        this.editor = editor;
        this.createdAt = createdAt;
        this.read = false;
        this.expirationTime = LocalDateTime.now().plusDays(7);
    }

    public void readNotification() {
        this.read = true;
        this.expirationTime = LocalDateTime.now().plusDays(3);
    }
}
