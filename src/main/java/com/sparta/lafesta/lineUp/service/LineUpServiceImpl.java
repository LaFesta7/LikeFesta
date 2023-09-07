    package com.sparta.lafesta.lineUp.service;

    import com.sparta.lafesta.common.exception.UnauthorizedException;
    import com.sparta.lafesta.festival.entity.Festival;
    import com.sparta.lafesta.festival.repository.FestivalRepository;
    import com.sparta.lafesta.festival.service.FestivalService;
    import com.sparta.lafesta.lineUp.dto.LineUpRequestDto;
    import com.sparta.lafesta.lineUp.dto.LineUpResponseDto;
    import com.sparta.lafesta.lineUp.entity.LineUp;
    import com.sparta.lafesta.lineUp.repository.LineUpRepository;
    import com.sparta.lafesta.user.entity.User;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    @RequiredArgsConstructor
    public class LineUpServiceImpl implements LineUpService {

        private final LineUpRepository lineUpRepository;
        private final FestivalRepository festivalRepository;
        private final FestivalService festivalService;

        @Override
        @Transactional
        public LineUpResponseDto saveLineUp(Long festivalId, LineUpRequestDto requestDto, User user) {
            // 사용자에 대한 페스티벌을 확인
            Festival festival = getFestivalForUser(festivalId, user);
            LineUp savedLineUp = lineUpRepository.save(requestDto.toEntity(festival));
            return new LineUpResponseDto(savedLineUp);
        }

        @Override
        @Transactional
        public LineUpResponseDto updateLineUp(Long festivalId, Long lineUpId, LineUpRequestDto requestDto, User user) {
            // 페스티벌 및 사용자 권한 확인
            getFestivalForUser(festivalId, user);

            return lineUpRepository.findById(lineUpId)
                    .map(lineUp -> {
                        lineUp.update(requestDto.getArtistName(), requestDto.getArtistImage(), requestDto.getArtistGenre(), requestDto.getArtistDescription());
                        return new LineUpResponseDto(lineUp);
                    })
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 라인업을 찾을 수 없습니다: " + lineUpId));
        }

        @Override
        @Transactional
        public void deleteLineUp(Long festivalId, Long lineUpId, User user) {
            // 페스티벌 및 사용자 권한 확인
            getFestivalForUser(festivalId, user);
            if (!lineUpRepository.existsById(lineUpId)) {
                throw new IllegalArgumentException("해당 ID의 라인업을 찾을 수 없습니다: " + lineUpId);
            }
            lineUpRepository.deleteById(lineUpId);
        }

        @Override
        @Transactional(readOnly = true)
        public LineUpResponseDto findLineUps(Long festivalId, Long lineUpId) {
            LineUp lineUp = lineUpRepository.findById(lineUpId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 라인업을 찾을 수 없습니다: " + lineUpId));
            return new LineUpResponseDto(lineUp);
        }

        @Override
        public LineUpResponseDto saveLineUp(Long festivalId, LineUpRequestDto lineUpRequestDto) {
            return null;
        }

        @Override
        public LineUpResponseDto updateLineUp(Long festivalId, Long lineUpId, LineUpRequestDto lineUpRequestDto) {
            return null;
        }

        @Override
        public void deleteLineUp(Long festivalId, Long lineUpId) {

        }

        private Festival getFestivalForUser(Long festivalId, User user) {
            Festival festival = festivalRepository.findById(festivalId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 페스티벌을 찾을 수 없습니다: " + festivalId));

            if (!festivalService.isFestivalAdmin(festival, user)) {
                throw new UnauthorizedException("필요한 권한이 없습니다.");
            }
            return festival;
        }
    }
