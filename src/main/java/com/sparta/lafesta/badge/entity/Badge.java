package com.sparta.lafesta.badge.entity;

import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Column(name = "condition", nullable = false)
    private BadgeConditionEnum condition;

    @Column(name = "first_day", nullable = false)
    private LocalDate firstDay;

    @Column(name = "last_day", nullable = false)
    private LocalDate lastDay;

    @Column(name = "standard", nullable = false)
    private Long standard;

    public Badge(BadgeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.condition = requestDto.getCondition();
        this.firstDay = requestDto.getFirstDay();
        this.lastDay = requestDto.getLastDay();
        this.standard = requestDto.getStandard();
    }
}
