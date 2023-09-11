package com.sparta.lafesta.lineUp.entity;

import com.sparta.lafesta.festival.entity.Festival;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "line_up")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "preview_url", nullable = false)
    private String previewUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    public LineUp(String title, String artist, String imageUrl, String previewUrl, Festival festival) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.previewUrl = previewUrl;
        this.festival = festival;
    }

    public void update(String title, String artist, String imageUrl, String previewUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.previewUrl = previewUrl;
    }
}