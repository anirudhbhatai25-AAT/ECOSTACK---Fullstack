package com.lnt.fullstack.controller;

import com.lnt.fullstack.model.EcoReport;
import com.lnt.fullstack.repository.EcoReportRepository;
import com.lnt.fullstack.service.EcoAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class PortalController {

    private final EcoAnalyzerService analyzerService;
    private final EcoReportRepository ecoReportRepository;

    public PortalController(EcoAnalyzerService analyzerService, EcoReportRepository ecoReportRepository) {
        this.analyzerService = analyzerService;
        this.ecoReportRepository = ecoReportRepository;
    }

    // 1. CREATE / ANALYZE (POST)
    @PostMapping("/analyze")
    public ResponseEntity<EcoReport> processCode(@RequestBody AnalysisRequest request) {
        long scale = request.getExecutionScale() > 0 ? request.getExecutionScale() : 1_000_000L;
        EcoReport report = analyzerService.analyzeAnyLanguage(
                request.getRawJavaCode(),
                request.getLanguage(),
                scale
        );
        EcoReport savedReport = ecoReportRepository.save(report);
        return ResponseEntity.ok(savedReport);
    }

    // 2. READ ALL (GET)
    @GetMapping("/reports")
    public List<EcoReport> getAllReports() {
        return ecoReportRepository.findAll();
    }

    // 3. READ BY ID (GET)
    @GetMapping("/reports/{id}")
    public ResponseEntity<EcoReport> getReportById(@PathVariable Long id) {
        return ecoReportRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. UPDATE (PUT)
    @PutMapping("/reports/{id}")
    public ResponseEntity<EcoReport> updateReport(@PathVariable Long id, @RequestBody EcoReport reportDetails) {
        return ecoReportRepository.findById(id).map(existingReport -> {
            existingReport.setSuggestion(reportDetails.getSuggestion());
            existingReport.setOptimizedCode(reportDetails.getOptimizedCode());
            EcoReport updatedReport = ecoReportRepository.save(existingReport);
            return ResponseEntity.ok(updatedReport);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETE (DELETE)
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        if (ecoReportRepository.existsById(id)) {
            ecoReportRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Request DTO Class
    public static class AnalysisRequest {
        private String rawJavaCode;
        private String language;
        private long executionScale;

        public AnalysisRequest() {}

        public String getRawJavaCode() { return rawJavaCode; }
        public void setRawJavaCode(String rawJavaCode) { this.rawJavaCode = rawJavaCode; }

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }

        public long getExecutionScale() { return executionScale; }
        public void setExecutionScale(long executionScale) { this.executionScale = executionScale; }
    }
}