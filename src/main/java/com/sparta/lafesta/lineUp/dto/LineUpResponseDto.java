package com.sparta.lafesta.lineUp.dto;

import com.sparta.lafesta.lineUp.entity.LineUp;
import lombok.Getter;

@Getter
public class LineUpResponseDto {
    private Long id;
    private String title;
    private String artist;
    private String imageUrl;
    private String previewUrl;

    public LineUpResponseDto(LineUp lineUp) {
        this.id = lineUp.getId();
        this.title = lineUp.getTitle();
        this.artist = lineUp.getArtist();
        this.imageUrl = lineUp.getImageUrl();
        this.previewUrl = lineUp.getPreviewUrl();
    }
}
