package com.example.cppoptimizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodeOptimizerService {

    private static final Logger logger = LoggerFactory.getLogger(CodeOptimizerService.class);

    public enum Language {
        C, CPP
    }

    public static class MemoryUsage {
        private long heapSize;
        private long stackSize;

        public MemoryUsage(long heapSize, long stackSize) {
            this.heapSize = heapSize;
            this.stackSize = stackSize;
        }

        public long getHeapSize() {
            return heapSize;
        }

        public long getStackSize() {
            return stackSize;
        }
    }

    public static class TimingEntry {
        private String step;
        private double timeMs;

        public TimingEntry(String step, double timeMs) {
            this.step = step;
            this.timeMs = timeMs;
        }

        public String getStep() {
            return step;
        }

        public double getTimeMs() {
            return timeMs;
        }
    }

    public static class OptimizationResult {
        private String optimizedCode;
        private MemoryUsage beforeMemory;
        private MemoryUsage afterMemory;
        private List<TimingEntry> timingEntries;
        private List<String> optimizationInsights; // New field for insights

        public OptimizationResult(String optimizedCode, MemoryUsage beforeMemory, MemoryUsage afterMemory, List<TimingEntry> timingEntries, List<String> optimizationInsights) {
            this.optimizedCode = optimizedCode;
            this.beforeMemory = beforeMemory;
            this.afterMemory = afterMemory;
            this.timingEntries = timingEntries;
            this.optimizationInsights = optimizationInsights;
        }

        public String getOptimizedCode() { return optimizedCode; }
        public MemoryUsage getBeforeMemory() { return beforeMemory; }
        public MemoryUsage getAfterMemory() { return afterMemory; }
        public List<TimingEntry> getTimingEntries() { return timingEntries; }
        public List<String> getOptimizationInsights() { return optimizationInsights; }
    }

    public OptimizationResult optimize(String code) {
        long startTotalTime = System.nanoTime();
        List<TimingEntry> timingEntries = new ArrayList<>();
        List<String> optimizationInsights = new ArrayList<>(); // Initialize insights list
        Language lang = detectLanguage(code);
        logger.info("Detected language: {}", lang);

        MemoryUsage beforeMemory = estimateMemoryUsage(code, lang);

        long startTime, endTime;

        startTime = System.nanoTime();
        String newCode = foldConstants(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Applied constant folding to simplify arithmetic expressions.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double foldConstantsTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("foldConstants", foldConstantsTime));

        startTime = System.nanoTime();
        newCode = optimizeArithmeticLoops(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Optimized arithmetic loop to direct assignment.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double optimizeLoopsTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("optimizeArithmeticLoops", optimizeLoopsTime));

        startTime = System.nanoTime();
        newCode = eliminateDeadCode(code, lang);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Eliminated unused variables (dead code).");
        }
        code = newCode;
        endTime = System.nanoTime();
        double eliminateDeadCodeTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("eliminateDeadCode", eliminateDeadCodeTime));

        startTime = System.nanoTime();
        newCode = optimizeMemoryAllocation(code, lang);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Converted heap allocations to stack where possible.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double optimizeMemoryTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("optimizeMemoryAllocation", optimizeMemoryTime));

        MemoryUsage afterMemory = estimateMemoryUsage(code, lang);

        long endTotalTime = System.nanoTime();
        double totalTime = (endTotalTime - startTotalTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("Total", totalTime));

        return new OptimizationResult(code, beforeMemory, afterMemory, timingEntries, optimizationInsights);
    }

    private Language detectLanguage(String code) {
        if (code.contains("iostream") || code.contains("using namespace std;") || code.contains("cout") || code.contains("cin")) {
            return Language.CPP;
        }
        return Language.C;
    }

    private MemoryUsage estimateMemoryUsage(String code, Language lang) {
        long heapSize = 0;
        long stackSize = 0;

        // Estimate heap usage (malloc, new)
        Pattern mallocPattern = Pattern.compile("malloc\\s*\\(\\s*(\\d+)\\s*\\*\\s*sizeof\\s*\\(\\s*\\w+\\s*\\)\\s*\\)");
        Matcher mallocMatcher = mallocPattern.matcher(code);
        while (mallocMatcher.find()) {
            heapSize += Long.parseLong(mallocMatcher.group(1)) * 4; // Assuming int size
        }

        Pattern newPattern = Pattern.compile("new\\s+\\w+\\s*\\[\\s*(\\d+)\\s*\\]");
        Matcher newMatcher = newPattern.matcher(code);
        while (newMatcher.find()) {
            heapSize += Long.parseLong(newMatcher.group(1)) * 4; // Assuming int size
        }

        // Estimate stack usage (variable declarations)
        Pattern varPattern = Pattern.compile("(int|float|double|char)\\s+\\w+(\\s*=\\s*[^;]+)?;");
        Matcher varMatcher = varPattern.matcher(code);
        while (varMatcher.find()) {
            String type = varMatcher.group(1);
            switch (type) {
                case "int":
                    stackSize += 4;
                    break;
                case "float":
                    stackSize += 4;
                    break;
                case "double":
                    stackSize += 8;
                    break;
                case "char":
                    stackSize += 1;
                    break;
            }
        }

        return new MemoryUsage(heapSize, stackSize);
    }

    private String foldConstants(String code) {
        Pattern pattern = Pattern.compile("(\\d+)\\s*\\+\\s*(\\d+)");
        Matcher matcher = pattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));
            matcher.appendReplacement(result, String.valueOf(a + b));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String optimizeArithmeticLoops(String code) {
        Pattern pattern = Pattern.compile("for\\s*\\(\\s*int\\s+i\\s*=\\s*0\\s*;\\s*i\\s*<\\s*(\\d+)\\s*;\\s*i\\s*\\+\\+\\s*\\)\\s*\\{\\s*sum\\s*\\+=\\s*i\\s*;\\s*\\}");
        Matcher matcher = pattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int n = Integer.parseInt(matcher.group(1));
            int sum = (n * (n - 1)) / 2;
            matcher.appendReplacement(result, "sum = " + sum + ";");
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String eliminateDeadCode(String code, Language lang) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();
        List<String> variables = new ArrayList<>();
        List<String> usedVariables = new ArrayList<>();

        // Collect variable declarations
        Pattern varPattern = Pattern.compile("(int|float|double|char)\\s+(\\w+)(\\s*=\\s*[^;]+)?;");
        for (String line : lines) {
            Matcher matcher = varPattern.matcher(line);
            if (matcher.find()) {
                variables.add(matcher.group(2));
            }
        }

        // Find used variables
        for (String line : lines) {
            for (String var : variables) {
                if (line.contains(var) && !line.matches(".*(int|float|double|char)\\s+" + var + "(\\s*=\\s*[^;]+)?;.*")) {
                    usedVariables.add(var);
                }
            }
        }

        // Keep only lines with used variables or non-variable lines
        for (String line : lines) {
            boolean keep = true;
            Matcher matcher = varPattern.matcher(line);
            if (matcher.find()) {
                String varName = matcher.group(2);
                if (!usedVariables.contains(varName)) {
                    keep = false;
                }
            }
            if (keep) {
                newLines.add(line);
            }
        }

        return String.join("\n", newLines);
    }

    private String optimizeMemoryAllocation(String code, Language lang) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();

        Pattern mallocPattern = Pattern.compile("(\\w+)\\s*=\\s*\\(\\w+\\*\\)\\s*malloc\\s*\\(\\s*(\\d+)\\s*\\*\\s*sizeof\\s*\\(\\s*\\w+\\s*\\)\\s*\\);");
        Pattern newPattern = Pattern.compile("(\\w+)\\s*=\\s*new\\s+\\w+\\s*\\[\\s*(\\d+)\\s*\\];");

        for (String line : lines) {
            Matcher mallocMatcher = mallocPattern.matcher(line);
            Matcher newMatcher = newPattern.matcher(line);

            if (mallocMatcher.find()) {
                String varName = mallocMatcher.group(1);
                int size = Integer.parseInt(mallocMatcher.group(2));
                if (size <= 10) { // Arbitrary threshold for stack allocation
                    newLines.add("int " + varName + "[" + size + "];");
                    code = code.replaceAll("free\\s*\\(\\s*" + varName + "\\s*\\);", "");
                } else {
                    newLines.add(line);
                }
            } else if (newMatcher.find()) {
                String varName = newMatcher.group(1);
                int size = Integer.parseInt(newMatcher.group(2));
                if (size <= 10) {
                    newLines.add("int " + varName + "[" + size + "];");
                    code = code.replaceAll("delete\\s*\\[\\s*\\]\\s*" + varName + "\\s*;", "");
                } else {
                    newLines.add(line);
                }
            } else {
                newLines.add(line);
            }
        }

        return String.join("\n", newLines);
    }
}