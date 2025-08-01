package com.devbyteschool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResumeAnalysisResult {

    @JsonProperty("overall_match_percentage")
    private int overallMatchPercentage;

    @JsonProperty("ats_score")
    private int atsScore;

    @JsonProperty("technical_skills_match")
    private int technicalSkillsMatch;

    @JsonProperty("experience_level_match")
    private int experienceLevelMatch;

    @JsonProperty("soft_skills_match")
    private int softSkillsMatch;

    @JsonProperty("industry_relevance")
    private int industryRelevance;

    @JsonProperty("matched_skills")
    private List<String> matchedSkills;

    @JsonProperty("missing_critical_skills")
    private List<String> missingCriticalSkills;

    @JsonProperty("experience_assessment")
    private String experienceAssessment;

    @JsonProperty("improvement_recommendations")
    private List<String> improvementRecommendations;

    @JsonProperty("semantic_insights")
    private String semanticInsights;

    @JsonProperty("confidence_score")
    private double confidenceScore;

    @JsonProperty("reasoning")
    private String reasoning;

    @JsonProperty("debug_info")
    private DebugInfo debugInfo;

    // Getters and Setters

    public int getOverallMatchPercentage() {
        return overallMatchPercentage;
    }

    public void setOverallMatchPercentage(int overallMatchPercentage) {
        this.overallMatchPercentage = overallMatchPercentage;
    }

    public int getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(int atsScore) {
        this.atsScore = atsScore;
    }

    public int getTechnicalSkillsMatch() {
        return technicalSkillsMatch;
    }

    public void setTechnicalSkillsMatch(int technicalSkillsMatch) {
        this.technicalSkillsMatch = technicalSkillsMatch;
    }

    public int getExperienceLevelMatch() {
        return experienceLevelMatch;
    }

    public void setExperienceLevelMatch(int experienceLevelMatch) {
        this.experienceLevelMatch = experienceLevelMatch;
    }

    public int getSoftSkillsMatch() {
        return softSkillsMatch;
    }

    public void setSoftSkillsMatch(int softSkillsMatch) {
        this.softSkillsMatch = softSkillsMatch;
    }

    public int getIndustryRelevance() {
        return industryRelevance;
    }

    public void setIndustryRelevance(int industryRelevance) {
        this.industryRelevance = industryRelevance;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingCriticalSkills() {
        return missingCriticalSkills;
    }

    public void setMissingCriticalSkills(List<String> missingCriticalSkills) {
        this.missingCriticalSkills = missingCriticalSkills;
    }

    public String getExperienceAssessment() {
        return experienceAssessment;
    }

    public void setExperienceAssessment(String experienceAssessment) {
        this.experienceAssessment = experienceAssessment;
    }

    public List<String> getImprovementRecommendations() {
        return improvementRecommendations;
    }

    public void setImprovementRecommendations(List<String> improvementRecommendations) {
        this.improvementRecommendations = improvementRecommendations;
    }

    public String getSemanticInsights() {
        return semanticInsights;
    }

    public void setSemanticInsights(String semanticInsights) {
        this.semanticInsights = semanticInsights;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public DebugInfo getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(DebugInfo debugInfo) {
        this.debugInfo = debugInfo;
    }

    public static class DebugInfo {

        @JsonProperty("name")
        private String name;

        @JsonProperty("processing_time_ms")
        private long processingTimeMs;

        @JsonProperty("resume_length")
        private int resumeLength;

        @JsonProperty("jd_length")
        private int jdLength;

        @JsonProperty("email")
        private String email;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getProcessingTimeMs() {
            return processingTimeMs;
        }

        public void setProcessingTimeMs(long processingTimeMs) {
            this.processingTimeMs = processingTimeMs;
        }

        public int getResumeLength() {
            return resumeLength;
        }

        public void setResumeLength(int resumeLength) {
            this.resumeLength = resumeLength;
        }

        public int getJdLength() {
            return jdLength;
        }

        public void setJdLength(int jdLength) {
            this.jdLength = jdLength;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
