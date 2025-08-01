package com.devbyteschool.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FallbackMatcherService {

    public Map<String, Object> match(String resumeText, String jobDescription) {
        if (resumeText == null || jobDescription == null) {
            return Map.of(
                "overall_match_percentage", 0.0,
                "matched_keywords", List.of(),
                "missing_keywords", List.of(),
                "confidence_score", 0.0,
                "fallback_used", true,
                "error", "Empty resume or job description"
            );
        }

        Set<String> jdWords = new HashSet<>(Arrays.asList(jobDescription.toLowerCase().split("\\W+")));
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        String lowerResume = resumeText.toLowerCase();

        int matches = 0;
        for (String word : jdWords) {
            if (word.length() > 3) {
                if (lowerResume.contains(word)) {
                    matched.add(word);
                    matches++;
                } else {
                    missing.add(word);
                }
            }
        }

        double matchPercent = jdWords.isEmpty() ? 0.0 : 100.0 * matches / jdWords.size();

        return new HashMap<>() {{
            put("overall_match_percentage", matchPercent);
            put("confidence_score", Math.min(matchPercent + 10, 100));
            put("matched_keywords", matched);
            put("missing_keywords", missing);
            put("fallback_used", true);
        }};
    }
}
