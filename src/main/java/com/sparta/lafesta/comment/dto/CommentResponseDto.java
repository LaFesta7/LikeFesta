package com.sparta.lafesta.comment.dto;

import com.sparta.lafesta.comment.entity.Comment;
import com.sparta.lafesta.common.entity.StringFormatter;
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
        this.festivalTitle = StringFormatter.format(comment.getReview().getFestival().getTitle());
        this.reviewTitle = StringFormatter.format(comment.getReview().getTitle());
        this.userId = comment.getUser().getId();
        this.userNickname = comment.getUser().getNickname();
        this.username = comment.getUser().getUsername();
        this.content = StringFormatter.format(comment.getContent());
        this.likeCnt = comment.getCommentLikes().size();
    }
}
