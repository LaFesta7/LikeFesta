package com.sparta.lafesta.badge.entity;

import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "cndtn", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BadgeConditionEnum cndtn;

    @Column(name = "first_day", nullable = false)
    private LocalDate firstDay;

    @Column(name = "last_day", nullable = false)
    private LocalDate lastDay;

    @Column(name = "standard", nullable = false)
    private Long standard;

    @OneToMany(mappedBy = "badge", orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

    public Badge(BadgeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.cndtn = requestDto.getCndtn();
        this.firstDay = requestDto.getFirstDay();
        this.lastDay = requestDto.getLastDay();
        this.standard = requestDto.getStandard();
    }

    public void modify(BadgeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.cndtn = requestDto.getCndtn();
        this.firstDay = requestDto.getFirstDay();
        this.lastDay = requestDto.getLastDay();
        this.standard = requestDto.getStandard();
    }
}
