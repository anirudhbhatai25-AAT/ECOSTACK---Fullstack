package com.lnt.fullstack.service;

import com.lnt.fullstack.model.EcoReport;

public interface EcoAnalyzerService {
    EcoReport analyzeAnyLanguage(String userCode, String language, long executionScale);
}