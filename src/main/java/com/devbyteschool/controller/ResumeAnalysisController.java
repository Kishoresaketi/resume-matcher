package com.devbyteschool.controller;

import com.devbyteschool.dto.ResumeAnalysisResult;
import com.devbyteschool.service.PerplexityService;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ResumeAnalysisController {

    @Autowired
    private PerplexityService perplexityService;

    private final Tika tika = new Tika();  // For file text extraction

    @GetMapping("/")
    public String showForm() {
        return "index";  // Renders index.html
    }

    @PostMapping("/analyze")
    public String analyzeResume(
            @RequestParam("resumeFile") MultipartFile resumeFile,
            @RequestParam("jobDescription") String jobDescription,
            Model model) {

        if (resumeFile.isEmpty()) {
            model.addAttribute("error", "Please upload a resume file.");
            return "index";
        }

        try {
            // Extract text from the uploaded file
            String resumeText = tika.parseToString(resumeFile.getInputStream());

            // Analyze the extracted resume text
            ResumeAnalysisResult result = perplexityService.analyzeResumeMatch(resumeText, jobDescription);

            // Send result to Thymeleaf view
            model.addAttribute("result", result);
            return "resume-analysis";  // Renders resume-analysis.html

        } catch (IOException | TikaException e) {
            model.addAttribute("error", "Error processing resume file: " + e.getMessage());
            return "index";
        }
    }
}
