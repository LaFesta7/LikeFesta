package com.sparta.lafesta.comment.entity;

import com.sparta.lafesta.comment.dto.CommentRequestDto;
import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.like.commentLike.entity.CommentLike;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId", nullable = false)
    private Review review;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();


    @Builder
    public Comment(String content, User user, Review review) {
        this.content = content;
        this.user = user;
        this.review = review;
    }

    public Comment(Review review, CommentRequestDto requestDto, User user) {
        this.review = review;
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void modify(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

}
