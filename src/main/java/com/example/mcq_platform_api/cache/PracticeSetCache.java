package com.example.mcq_platform_api.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.mcq_platform_api.dto.response.PracticeSetResponse;

@Component
public class PracticeSetCache {

    private static final int MAX_CACHE_SIZE = 20;
    private final Map<String, PracticeSetResponse> cache =
            Collections.synchronizedMap(
                    new LinkedHashMap<String, PracticeSetResponse>(MAX_CACHE_SIZE, 0.75f, true) {
                        @Override
                        protected boolean removeEldestEntry(Map.Entry<String, PracticeSetResponse> eldest) {
                            return size() > MAX_CACHE_SIZE;
                        }
                    }
            );


    public void cachePracticeSet(String sessionId, PracticeSetResponse response) {
        cache.put(sessionId, response);
    }

    public PracticeSetResponse getCachedPracticeSet(String sessionId) {
        return cache.get(sessionId);
    }

    public void clearCache(String sessionId) {
        cache.remove(sessionId);
    }

}
