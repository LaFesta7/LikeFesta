package com.sparta.lafesta.review.dto;

import com.sparta.lafesta.comment.dto.CommentResponseDto;
import com.sparta.lafesta.common.entity.DurationFormatter;
import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.review.entity.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewResponseDto {
    private Long id;
    private Long festivalId;
    private String festivalTitle;
    private String userNickname;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private String createdAtTimeAgo;
    private List<CommentResponseDto> comments;
    private List<FileOnS3Dto> files;
    private String fileName;
    private String fileUrl;
    private int likeCnt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.createdAtTimeAgo = DurationFormatter.format(review.getCreatedAt());
        this.festivalId = review.getFestival().getId();
        this.festivalTitle = review.getFestival().getTitle();
        this.userId = review.getUser().getId();
        this.userNickname = review.getUser().getNickname();
        this.username = review.getUser().getUsername();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.comments = review.getComments().stream().
                map(CommentResponseDto::new).toList();
        this.files = review.getReviewFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
        this.fileName = files.size() > 0 ? files.get(0).getKeyName() : "image";
        this.fileUrl = files.size() > 0 ? files.get(0).getUploadFileUrl() : "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblaeMK%2Fbtsuk3M5crm%2FalBRJvpmyK9Vj7Ae2Qw8p0%2Fimg.png";
        this.likeCnt = review.getReviewLikes().size();
    }
}
