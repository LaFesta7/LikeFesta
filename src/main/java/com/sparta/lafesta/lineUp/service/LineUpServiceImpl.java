package com.sparta.lafesta.lineUp.service;

import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.lineUp.repository.LineUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LineUpServiceImpl implements LineUpService {
    private final LineUpRepository lineUpRepository;
    private final FestivalRepository festivalRepository;

}
