package com.sparta.lafesta.like.festivalLike.entity;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "festivalLike")
public class FestivalLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festivalId")
    private Festival festival;

    public FestivalLike(User user, Festival festival) {
        this.user = user;
        this.festival = festival;
    }
}
