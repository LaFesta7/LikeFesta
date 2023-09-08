package com.sparta.lafesta.lineUp.controller;

import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.lineUp.dto.LineUpRequestDto;
import com.sparta.lafesta.lineUp.dto.LineUpResponseDto;
import com.sparta.lafesta.lineUp.service.LineUpService;
import com.sparta.lafesta.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "라인업 관련 API", description = "라인업 관련 API입니다.")
public class LineUpController {
    private final LineUpService lineUpService;

    @GetMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 조회", description = "페스티벌 라인업을 조회합니다.")
    public ResponseEntity<LineUpResponseDto> findLineUps(@PathVariable Long festivalId, @PathVariable Long lineUpId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            User user = userDetails.getUser();
            LineUpResponseDto lineUp = lineUpService.findLineUps(festivalId, lineUpId, user);
            return ResponseEntity.ok(lineUp);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/festivals/{festivalId}/lineups")
    @Operation(summary = "페스티벌 라인업 저장", description = "페스티벌 라인업을 저장합니다.")
    public ResponseEntity<LineUpResponseDto> saveLineUp(@PathVariable Long festivalId, @RequestBody LineUpRequestDto lineUpRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        LineUpResponseDto savedLineUp = lineUpService.saveLineUp(festivalId, lineUpRequestDto, user);
        return new ResponseEntity<>(savedLineUp, HttpStatus.CREATED);
    }

    @PutMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 수정", description = "페스티벌 라인업을 수정합니다.")
    public ResponseEntity<LineUpResponseDto> updateLineUp(@PathVariable Long festivalId, @PathVariable Long lineUpId, @RequestBody LineUpRequestDto lineUpRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        LineUpResponseDto updatedLineUp = lineUpService.updateLineUp(festivalId, lineUpId, lineUpRequestDto, user);
        return new ResponseEntity<>(updatedLineUp, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 삭제", description = "페스티벌 라인업을 삭제합니다.")
    public ResponseEntity<Void> deleteLineUp(@PathVariable Long festivalId, @PathVariable Long lineUpId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        lineUpService.deleteLineUp(festivalId, lineUpId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
