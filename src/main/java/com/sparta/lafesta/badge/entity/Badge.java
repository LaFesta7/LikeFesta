package com.sparta.lafesta.badge.entity;

import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.common.s3.entity.BadgeFileOnS3;
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

    @Column(name = "condition_enum", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BadgeConditionEnum conditionEnum;

    @Column(name = "condition_first_day")
    private LocalDate conditionFirstDay;

    @Column(name = "condition_last_day")
    private LocalDate conditionLastDay;

    @Column(name = "condition_standard", nullable = false)
    private Long conditionStandard;

    @OneToMany(mappedBy = "badge", orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

    @OneToMany(mappedBy = "badge", orphanRemoval = true)
    private List<BadgeFileOnS3> badgeFileOnS3s = new ArrayList<>();

    public Badge(BadgeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.conditionEnum = requestDto.getConditionEnum();
        this.conditionFirstDay = requestDto.getConditionFirstDay();
        this.conditionLastDay = requestDto.getConditionLastDay();
        this.conditionStandard = requestDto.getConditionStandard();
    }

    public void modify(BadgeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.conditionEnum = requestDto.getConditionEnum();
        this.conditionFirstDay = requestDto.getConditionFirstDay();
        this.conditionLastDay = requestDto.getConditionLastDay();
        this.conditionStandard = requestDto.getConditionStandard();
    }
}
