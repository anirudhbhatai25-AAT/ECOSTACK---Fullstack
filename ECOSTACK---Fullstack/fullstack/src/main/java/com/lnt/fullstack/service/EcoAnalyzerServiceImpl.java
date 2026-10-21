package com.lnt.fullstack.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.lnt.fullstack.model.EcoReport;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EcoAnalyzerServiceImpl implements EcoAnalyzerService {

    // Industry FinOps Data Center Benchmarks
    private static final double CPU_POWER_WATTS = 15.0;        // Typical vCPU power draw
    private static final double PUE = 1.2;                      // Data center Power Usage Effectiveness
    private static final double CARBON_INTENSITY_G_KWH = 708.0;    // Carbon intensity factor (g CO2e/kWh)
    private static final double ELECTRICITY_RATE_INR = 8.50;     // Commercial electricity rate per kWh (INR)

    @Override
    public EcoReport analyzeAnyLanguage(String userCode, String language, long executionScale) {
        if (userCode == null || userCode.trim().isEmpty()) {
            EcoReport emptyReport = new EcoReport();
            emptyReport.setDetectedLanguage(language != null ? language.toUpperCase() : "UNKNOWN");
            emptyReport.setSuggestion("Empty source code submitted. No energy metrics calculated.");
            emptyReport.setCarbonEmissions("0.0000 g CO2e");
            emptyReport.setElectricityCost("Rs. 0.00");
            emptyReport.setOptimizedCode("// No code provided.");
            return emptyReport;
        }

        int loopCount = 0;
        int memoryCount = 0;
        String targetLang = (language != null) ? language.toLowerCase() : "java";
        String optimizedOutput = userCode;

        if (targetLang.equals("java")) {
            try {
                // Primary Engine: Try parsing as full Java class AST
                CompilationUnit cu = StaticJavaParser.parse(userCode);

                loopCount = cu.findAll(ForStmt.class).size() 
                          + cu.findAll(WhileStmt.class).size() 
                          + cu.findAll(DoStmt.class).size();

                memoryCount = cu.findAll(ObjectCreationExpr.class).size();

                // Inject Inline AST Refactoring Directives
                for (ObjectCreationExpr objExpr : cu.findAll(ObjectCreationExpr.class)) {
                    if (objExpr.findAncestor(ForStmt.class).isPresent() || objExpr.findAncestor(WhileStmt.class).isPresent()) {
                        objExpr.setComment(new LineComment(" GREEN OPTIMIZATION: Hoist object creation outside hot loop boundary to eliminate GC thrashing "));
                    }
                }

                for (ForStmt forLoop : cu.findAll(ForStmt.class)) {
                    forLoop.setComment(new LineComment(" GREEN STRATEGY: Consider converting imperative loop to Parallel Streams for multi-core vectorization "));
                }

                optimizedOutput = cu.toString();

            } catch (Exception parseException) {
                // Secondary Engine: Lexical Guard fallback for partial Java snippets (e.g., int data = 100;)
                loopCount = countMatches(userCode, "\\b(for|while|do)\\b");
                memoryCount = countMatches(userCode, "\\bnew\\b");
                
                // Flexible check for valid Java syntax constructs (types, assignments, operators, controls)
                boolean matchesJavaTokens = Pattern.compile(
                    "\\b(int|double|float|long|short|byte|boolean|char|String|var|val|void|public|private|protected|static|final|class|interface|enum|import|return|if|else|switch|case)\\b|;|="
                ).matcher(userCode).find();

                // If input lacks any Java syntax tokens and has 0 AST nodes (e.g. "hi how are you")
                if (!matchesJavaTokens && loopCount == 0 && memoryCount == 0) {
                    EcoReport invalidReport = new EcoReport();
                    invalidReport.setDetectedLanguage("JAVA (INVALID)");
                    invalidReport.setLoopsFound(0);
                    invalidReport.setMemoryAllocations(0);
                    invalidReport.setCarbonEmissions("0.0000 g CO2e");
                    invalidReport.setElectricityCost("Rs. 0.00");
                    invalidReport.setSuggestion("--- Audit Error ---\nInvalid Java syntax detected. Plain text or non-compilable input cannot be audited.");
                    invalidReport.setOptimizedCode("// Cannot perform AST analysis on non-Java plain text.");
                    return invalidReport;
                }

                optimizedOutput = "// AST NOTICE: Parsed via Lexical Guard (Partial Code Snippet).\n" + userCode;
            }
        } else {
            // Lexical Analysis for C++ and Python
            loopCount = countMatches(userCode, "\\b(for|while)\\b");
            memoryCount = countMatches(userCode, "\\b(new|malloc|append|extend)\\b");
            optimizedOutput = "// GREEN SUGGESTION (" + targetLang.toUpperCase() + "):\n" +
                              "// Optimize loop conditions and eliminate memory reallocations in hot execution paths.\n" + userCode;
        }

        // =======================================================================
        // Dynamic FinOps Energy, Cost & Carbon Engine
        // =======================================================================
        double baseExecutionTimeSec = 0.000005;               // 5 µs baseline
        double loopTimePenaltySec = loopCount * 0.00012;       // 120 µs per loop
        double memoryTimePenaltySec = memoryCount * 0.00008;   // 80 µs per heap allocation

        double totalExecutionTimePerRunSec = baseExecutionTimeSec + loopTimePenaltySec + memoryTimePenaltySec;

        // Scale energy draw dynamically by user's target execution count
        double totalRuntimeHours = (totalExecutionTimePerRunSec * executionScale) / 3600.0;
        double serverPowerKW = (CPU_POWER_WATTS / 1000.0) * PUE;
        double totalKwh = serverPowerKW * totalRuntimeHours;

        double carbonFootprintGrams = totalKwh * CARBON_INTENSITY_G_KWH;
        double costInRupees = totalKwh * ELECTRICITY_RATE_INR;

        // Populate Report Output Model
        EcoReport report = new EcoReport();
        report.setDetectedLanguage(targetLang.toUpperCase());
        report.setLoopsFound(loopCount);
        report.setMemoryAllocations(memoryCount);
        report.setCarbonEmissions(String.format("%.4f g CO2e", carbonFootprintGrams));
        report.setElectricityCost(String.format("Rs. %.2f", costInRupees));
        report.setOptimizedCode(optimizedOutput);

        StringBuilder suggestionBuilder = new StringBuilder();
        suggestionBuilder.append("--- Green FinOps Energy Audit Report ---\n");
        suggestionBuilder.append("Evaluated Workload Scale: ").append(String.format("%,d Executions\n", executionScale));
        if (loopCount > 0) {
            suggestionBuilder.append("• High Iteration Intensity: Found ").append(loopCount).append(" AST loop node(s). Consider parallel stream vectorization.\n");
        }
        if (memoryCount > 0) {
            suggestionBuilder.append("• Heap Thrashing Risk: Found ").append(memoryCount).append(" heap allocation node(s). Hoist allocations out of loops.\n");
        }
        if (loopCount == 0 && memoryCount == 0) {
            suggestionBuilder.append("• Optimal Footprint: Minimal compute complexity detected across AST nodes.\n");
        }

        report.setSuggestion(suggestionBuilder.toString());
        return report;
    }

    private int countMatches(String text, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}