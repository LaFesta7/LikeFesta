package com.sparta.lafesta.festival.entity;

import com.sparta.lafesta.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "festivals")
public class Festival extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
