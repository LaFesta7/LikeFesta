package com.sparta.lafesta.follow.entity;

import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_follow")
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_target_id")
    private User followedUser;     // 팔로우 당하는 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User followingUser;      // 팔로우 하는 유저

    public UserFollow(User followedUser, User followingUser){
        this.followedUser = followedUser;
        this.followingUser = followingUser;
    }
}
