package com.sparta.lafesta.lineUp.controller;

import com.sparta.lafesta.lineUp.dto.LineUpRequestDto;
import com.sparta.lafesta.lineUp.dto.LineUpResponseDto;
import com.sparta.lafesta.lineUp.service.LineUpServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "라인업 관련 API", description = "라인업 관련 API입니다.")
public class LineUpController {
    private final LineUpServiceImpl lineUpService;

    @GetMapping("/festivals/{festivalId}/lineups")
    @Operation(summary = "페스티벌 라인업 조회", description = "페스티벌 라인업을 조회합니다.")
    public ResponseEntity<List<LineUpResponseDto>> findLineUps(@PathVariable Long festivalId) {
        List<LineUpResponseDto> lineUps = lineUpService.findLineUps(festivalId);
        return new ResponseEntity<>(lineUps, HttpStatus.OK);
    }

    @PostMapping("/festivals/{festivalId}/lineups")
    @Operation(summary = "페스티벌 라인업 저장", description = "페스티벌 라인업을 저장합니다.")
    public ResponseEntity<LineUpResponseDto> saveLineUp(@PathVariable Long festivalId, @RequestBody LineUpRequestDto lineUpRequestDto) throws IOException {
        LineUpResponseDto savedLineUp = lineUpService.saveLineUp(festivalId, lineUpRequestDto);
        return new ResponseEntity<>(savedLineUp, HttpStatus.CREATED);
    }

    @PutMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 수정", description = "페스티벌 라인업을 수정합니다.")
    public ResponseEntity<LineUpResponseDto> updateLineUp(@PathVariable Long festivalId, @PathVariable Long lineUpId, @RequestBody LineUpRequestDto lineUpRequestDto) throws IOException {
        LineUpResponseDto updatedLineUp = lineUpService.updateLineUp(festivalId, lineUpId, lineUpRequestDto);
        return new ResponseEntity<>(updatedLineUp, HttpStatus.OK);
    }

    @DeleteMapping("/festivals/{festivalId}/lineups/{lineUpId}")
    @Operation(summary = "페스티벌 라인업 삭제", description = "페스티벌 라인업을 삭제합니다.")
    public ResponseEntity<Void> deleteLineUp(@PathVariable Long festivalId, @PathVariable Long lineUpId) throws IOException {
        lineUpService.deleteLineUp(festivalId, lineUpId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
