package com.sparta.lafesta.tag.entity;

import com.sparta.lafesta.badge.entity.BadgeTag;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FestivalTag> festivals = new ArrayList<>();

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    private List<BadgeTag> badgeTags = new ArrayList<>();

    public Tag(TagRequestDto requestDto) {
        this.title = requestDto.getTitle();
    }

    public void modify(TagRequestDto requestDto) {
        this.title = requestDto.getTitle();
    }
}
