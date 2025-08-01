package com.devbyteschool.controller;

import com.devbyteschool.dto.ResumeAnalysisResult;
import com.devbyteschool.service.CacheService;
import com.devbyteschool.service.FallbackMatcherService;
import com.devbyteschool.service.PerplexityService;
import com.devbyteschool.utils.PDFUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/match")
public class ResumeMatcherController {

    private final PerplexityService perplexityService;
    private final CacheService cacheService;
    private final FallbackMatcherService fallbackMatcherService;

    public ResumeMatcherController(PerplexityService perplexityService,
                                   CacheService cacheService,
                                   FallbackMatcherService fallbackMatcherService) {
        this.perplexityService = perplexityService;
        this.cacheService = cacheService;
        this.fallbackMatcherService = fallbackMatcherService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> matchResume(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("job_description") String jobDescription,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        // Extract text from uploaded PDF
        String resumeText = PDFUtils.extractTextFromPdf(file);

        // Generate a unique cache key based on resume + JD
        String cacheKey = generateCacheKey(resumeText, jobDescription);

        // Check cache first
        Optional<Map<String, Object>> cachedOpt = Optional.ofNullable(cacheService.get(cacheKey));
        if (cachedOpt.isPresent()) {
            Map<String, Object> cached = new HashMap<>(cachedOpt.get());
            cached.put("cached", true);
            return ResponseEntity.ok(cached);
        }

        try {
            long startTime = System.currentTimeMillis();

            // Get ResumeAnalysisResult from service
            ResumeAnalysisResult analysisResult = perplexityService.analyzeResumeMatch(resumeText, jobDescription);

            // Convert ResumeAnalysisResult to Map<String,Object>
            Map<String, Object> analysis = convertResultToMap(analysisResult);

            long duration = System.currentTimeMillis() - startTime;

            // Add metadata
            analysis.put("cached", false);
            analysis.put("debug_info", Map.of(
                    "name", name,
                    "email", email,
                    "processing_time_ms", duration,
                    "resume_length", resumeText.length(),
                    "jd_length", jobDescription.length()
            ));

            // Save in cache
            cacheService.put(cacheKey, analysis);

            return ResponseEntity.ok(analysis);

        } catch (Exception e) {
            // Fallback logic if Perplexity fails
            Map<String, Object> fallbackResult = fallbackMatcherService.match(resumeText, jobDescription);
            fallbackResult.put("name", name);
            fallbackResult.put("email", email);
            fallbackResult.put("fallback_used", true);
            fallbackResult.put("error", e.getMessage());

            return ResponseEntity.ok(fallbackResult);
        }
    }

    private String generateCacheKey(String resumeText, String jobDescription) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String combined = resumeText + "|" + jobDescription;
        byte[] digest = md.digest(combined.getBytes());
        return Base64.getEncoder().encodeToString(digest);
    }

    // Helper method to convert ResumeAnalysisResult to Map<String,Object>
    private Map<String, Object> convertResultToMap(ResumeAnalysisResult result) {
        Map<String, Object> map = new HashMap<>();
        map.put("overallMatchPercentage", result.getOverallMatchPercentage());
        map.put("atsScore", result.getAtsScore());
        map.put("technicalSkillsMatch", result.getTechnicalSkillsMatch());
        map.put("experienceLevelMatch", result.getExperienceLevelMatch());
        map.put("softSkillsMatch", result.getSoftSkillsMatch());
        map.put("industryRelevance", result.getIndustryRelevance());
        map.put("matchedSkills", result.getMatchedSkills());
        map.put("missingCriticalSkills", result.getMissingCriticalSkills());
        map.put("experienceAssessment", result.getExperienceAssessment());
        map.put("improvementRecommendations", result.getImprovementRecommendations());
        return map;
    }

}
