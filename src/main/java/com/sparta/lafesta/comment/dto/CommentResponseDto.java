package com.sparta.lafesta.comment.dto;

import com.sparta.lafesta.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String festivalTitle;
    private String reviewTitle;
    private Long userId;
    private String userNickname;
    private String username;
    private String content;
    private int likeCnt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.festivalTitle = comment.getReview().getFestival().getTitle();
        this.reviewTitle = comment.getReview().getTitle();
        this.userId = comment.getUser().getId();
        this.userNickname = comment.getUser().getNickname();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.likeCnt = comment.getCommentLikes().size();
    }
}
