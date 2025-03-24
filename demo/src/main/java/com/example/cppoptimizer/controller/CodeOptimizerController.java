package com.example.cppoptimizer.controller;

import com.example.cppoptimizer.service.CodeOptimizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class CodeOptimizerController {

    @Autowired
    private CodeOptimizerService codeOptimizerService;

    @PostMapping("/optimize")
    public String optimizeCode(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please upload a file.");
            return "index";
        }

        try {
            String originalCode = new String(file.getBytes(), StandardCharsets.UTF_8);
            CodeOptimizerService.OptimizationResult result = codeOptimizerService.optimize(originalCode);

            model.addAttribute("originalCode", originalCode);
            model.addAttribute("optimizedCode", result.getOptimizedCode());
            model.addAttribute("beforeMemory", result.getBeforeMemory());
            model.addAttribute("afterMemory", result.getAfterMemory());
            model.addAttribute("timingEntries", result.getTimingEntries());
            model.addAttribute("optimizationInsights", result.getOptimizationInsights()); // Add insights
        } catch (IOException e) {
            model.addAttribute("error", "Error reading the file: " + e.getMessage());
        }

        return "index";
    }

    @PostMapping(value = "/optimizeEdited", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<?> optimizeEditedCode(@RequestBody String code) {
        try {
            CodeOptimizerService.OptimizationResult result = codeOptimizerService.optimize(code);
            return ResponseEntity.ok().body(new OptimizationResponse(result.getOptimizedCode(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OptimizationResponse(null, "Error optimizing code: " + e.getMessage()));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadCode(@RequestParam("code") String code, @RequestParam("filename") String filename) {
        byte[] codeBytes = code.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(codeBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(codeBytes.length)
                .body(resource);
    }

    private static class OptimizationResponse {
        private String optimizedCode;
        private String error;

        public OptimizationResponse(String optimizedCode, String error) {
            this.optimizedCode = optimizedCode;
            this.error = error;
        }

        public String getOptimizedCode() {
            return optimizedCode;
        }

        public String getError() {
            return error;
        }
    }
}