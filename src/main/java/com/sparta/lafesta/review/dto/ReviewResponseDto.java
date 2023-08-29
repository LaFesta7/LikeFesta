package com.sparta.lafesta.review.dto;

import com.sparta.lafesta.comment.dto.CommentResponseDto;
import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.review.entity.Review;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String festivalTitle;
    private String userNickname;
    private String title;
    private String content;
    private List<CommentResponseDto> comments;
    private List<FileOnS3Dto> files;
    private int likeCnt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.festivalTitle = review.getFestival().getTitle();
        this.userNickname = review.getUser().getNickname();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.comments = review.getComments().stream().
                map(CommentResponseDto::new).collect(Collectors.toList());
        this.files = review.getReviewFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
        this.likeCnt = review.getReviewLikes().size();
    }
}
