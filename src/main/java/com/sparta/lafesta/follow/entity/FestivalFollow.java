package com.sparta.lafesta.follow.entity;

import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "festival_follow")
public class FestivalFollow extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival followedFestival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User followingFestivalUser;

    public FestivalFollow(Festival followedFestival, User followingFestivalUser){
        this.followedFestival = followedFestival;
        this.followingFestivalUser = followingFestivalUser;
    }
}
