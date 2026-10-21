package com.lnt.fullstack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "eco_reports")
public class EcoReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detectedLanguage;
    private int loopsFound;
    private int memoryAllocations;
    private String carbonEmissions;
    private String electricityCost;

    @Lob
    private String suggestion;

    @Lob
    private String optimizedCode; // Field for AST transformed green code

    public EcoReport() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDetectedLanguage() { return detectedLanguage; }
    public void setDetectedLanguage(String detectedLanguage) { this.detectedLanguage = detectedLanguage; }

    public int getLoopsFound() { return loopsFound; }
    public void setLoopsFound(int loopsFound) { this.loopsFound = loopsFound; }

    public int getMemoryAllocations() { return memoryAllocations; }
    public void setMemoryAllocations(int memoryAllocations) { this.memoryAllocations = memoryAllocations; }

    public String getCarbonEmissions() { return carbonEmissions; }
    public void setCarbonEmissions(String carbonEmissions) { this.carbonEmissions = carbonEmissions; }

    public String getElectricityCost() { return electricityCost; }
    public void setElectricityCost(String electricityCost) { this.electricityCost = electricityCost; }

    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }

    public String getOptimizedCode() { return optimizedCode; }
    public void setOptimizedCode(String optimizedCode) { this.optimizedCode = optimizedCode; }
}