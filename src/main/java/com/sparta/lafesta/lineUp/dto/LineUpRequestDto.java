package com.sparta.lafesta.lineUp.dto;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.lineUp.entity.LineUp;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class LineUpRequestDto {
    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private String title;

    @NotBlank(message = "아티스트는 비워둘 수 없습니다.")
    private String artist;

    @NotBlank(message = "이미지 URL은 비워둘 수 없습니다.")
    private String imageUrl;

    @NotBlank(message = "미리보기 URL은 비워둘 수 없습니다.")
    private String previewUrl;

    public String getArtistName() {
        return this.artist;
    }

    public String getArtistImage() {
        return this.imageUrl;
    }

    public String getArtistGenre() {
        return this.previewUrl;
    }

    public String getArtistDescription() {
        return this.title;
    }

    public LineUp toEntity(Festival festival) {
        return new LineUp(title, artist, imageUrl, previewUrl, festival);
    }
}