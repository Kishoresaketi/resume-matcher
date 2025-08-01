package com.devbyteschool.service;



import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000; // 24 hours

    public Map<String, Object> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        cache.remove(key);
        return null;
    }

    public void put(String key, Map<String, Object> value) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis() + CACHE_EXPIRY_MS));
    }

    private static class CacheEntry {
        private final Map<String, Object> value;
        private final long expiryTime;

        public CacheEntry(Map<String, Object> value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public Map<String, Object> getValue() { return value; }

        public boolean isExpired() { return System.currentTimeMillis() > expiryTime; }
    }
}
