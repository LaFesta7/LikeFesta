package com.sparta.lafesta.lineUp.controller;

import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.lineUp.dto.LineUpRequestDto;
import com.sparta.lafesta.lineUp.dto.LineUpResponseDto;
import com.sparta.lafesta.lineUp.service.LineUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "라인업 관련 API", description = "라인업 관련 API입니다.")
public class LineUpController {
    private final LineUpService lineUpService;

    @GetMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 조회", description = "페스티벌 라인업을 조회합니다.")
    public ResponseEntity<LineUpResponseDto> findLineUps(@PathVariable Long festivalId, @PathVariable Long lineUpId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LineUpResponseDto lineUp = lineUpService.findLineUps(festivalId, lineUpId);
        return ResponseEntity.ok(lineUp);
    }

    @PostMapping("/festivals/{festivalId}/lineups")
    @Operation(summary = "페스티벌 라인업 저장", description = "페스티벌 라인업을 저장합니다.")
    public ResponseEntity<LineUpResponseDto> saveLineUp(@PathVariable Long festivalId, @RequestBody LineUpRequestDto lineUpRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        LineUpResponseDto savedLineUp = lineUpService.saveLineUp(festivalId, lineUpRequestDto, userId);
        return new ResponseEntity<>(savedLineUp, HttpStatus.CREATED);
    }

    @PutMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 수정", description = "페스티벌 라인업을 수정합니다.")
    public ResponseEntity<LineUpResponseDto> updateLineUp(@PathVariable Long festivalId, @PathVariable Long lineUpId, @RequestBody LineUpRequestDto lineUpRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        LineUpResponseDto updatedLineUp = lineUpService.updateLineUp(festivalId, lineUpId, lineUpRequestDto, userId);
        return new ResponseEntity<>(updatedLineUp, HttpStatus.OK);
    }

    @DeleteMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 삭제", description = "페스티벌 라인업을 삭제합니다.")
    public ResponseEntity<Void> deleteLineUp(@PathVariable Long festivalId, @PathVariable Long lineUpId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        lineUpService.deleteLineUp(festivalId, lineUpId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
