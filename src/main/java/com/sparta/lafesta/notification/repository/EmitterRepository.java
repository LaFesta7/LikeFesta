package com.sparta.lafesta.notification.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmitterRepository {

    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Long userId, SseEmitter emitter) {
        final String key = getKey(userId);
        log.info("Saved SseEmitter for {}", userId);
        emitterMap.put(key, emitter);
        return emitter;
    }

    public Optional<SseEmitter> get(Long userId) {
        SseEmitter result = emitterMap.get(getKey(userId));
        log.info("Got SseEmitter for {}", userId);
        return Optional.ofNullable(result);
    }

    public void delete(Long userId) {
        emitterMap.remove(getKey(userId));
        log.info("Deleted SseEmitter for {}", userId);
    }

    private String getKey(Long userId) {
        return "emitter:UID:" + userId;
    }

}
