package com.sparta.lafesta.review.entity;

import com.sparta.lafesta.comment.entity.Comment;
import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "reviews")
public class Review extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festivalId", nullable = false)
    private Festival festival;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Review(Festival festival, ReviewRequestDto requestDto, User user) {
        this.festival = festival;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void modify(ReviewRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

}
