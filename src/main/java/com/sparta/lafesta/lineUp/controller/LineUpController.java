package com.sparta.lafesta.lineUp.controller;

import com.sparta.lafesta.lineUp.service.LineUpServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "라인업 관련 API", description = "라인업 관련 API입니다.")
public class LineUpController {
    private final LineUpServiceImpl lineUpService;

    @GetMapping("/festivals/{festivalId}/lineups")
    @Operation(summary = "페스티벌 라인업 조회", description = "페스티벌 라인업을 조회합니다.")
    public
}
