package com.devbyteschool.service;

import com.devbyteschool.dto.ResumeAnalysisResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PerplexityService {

    @Value("${perplexity.api.key}")
    private String apiKey;

    @Value("${perplexity.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public ResumeAnalysisResult analyzeResumeMatch(String resumeText, String jobDescription) throws IOException {
        String prompt = buildPrompt(resumeText, jobDescription);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", "sonar-pro",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful career advisor and resume analyst."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, JsonNode.class);

        String content = response.getBody()
                .get("choices").get(0)
                .get("message")
                .get("content")
                .asText();

        String cleanedJson = stripMarkdownFormatting(content);

        try {
            return mapper.readValue(cleanedJson, ResumeAnalysisResult.class);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse JSON as ResumeAnalysisResult:");
            System.err.println(cleanedJson);
            throw new IOException("Failed to parse Perplexity response", e);
        }
    }

    private String buildPrompt(String resume, String jd) {
        return """
            Analyze the following resume against the job description.
            Provide an ATS-style structured JSON output with:
            - overall_match_percentage
            - ats_score
            - technical_skills_match
            - experience_level_match
            - soft_skills_match
            - industry_relevance
            - matched_skills
            - missing_critical_skills
            - experience_assessment
            - improvement_recommendations
            - semantic_insights
            - confidence_score
            - reasoning

            Output ONLY valid JSON without markdown formatting.

            Resume:
            %s

            Job Description:
            %s
            """.formatted(resume, jd);
    }

    private String stripMarkdownFormatting(String content) {
        return content
                .replaceAll("^```json\\s*", "")
                .replaceAll("```$", "")
                .replaceAll("^```", "")
                .trim();
    }
}
