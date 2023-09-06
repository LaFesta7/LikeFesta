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

    //스포티파이api로 받아온 음악리스트
    @Column(name = "music_list")
    private String musicList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    public LineUp(String musicList, Festival festival) {
        this.musicList = musicList;
        this.festival = festival;
    }
}