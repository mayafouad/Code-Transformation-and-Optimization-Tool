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
        private List<String> optimizationInsights;

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
        List<String> optimizationInsights = new ArrayList<>();
        Language lang = detectLanguage(code);
        logger.info("Detected language: {}", lang);

        MemoryUsage beforeMemory = estimateMemoryUsage(code, lang);

        long startTime, endTime;

        // 1. Constant Folding
        startTime = System.nanoTime();
        String newCode = foldConstants(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Applied constant folding to simplify arithmetic expressions.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double foldConstantsTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("foldConstants", foldConstantsTime));

        // 2. Optimize Arithmetic Loops
        startTime = System.nanoTime();
        newCode = optimizeArithmeticLoops(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Optimized arithmetic loop to direct assignment.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double optimizeLoopsTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("optimizeArithmeticLoops", optimizeLoopsTime));

        // 3. Eliminate Dead Code
        startTime = System.nanoTime();
        newCode = eliminateDeadCode(code, lang);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Eliminated unused variables (dead code).");
        }
        code = newCode;
        endTime = System.nanoTime();
        double eliminateDeadCodeTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("eliminateDeadCode", eliminateDeadCodeTime));

        // 4. Optimize Memory Allocation
        startTime = System.nanoTime();
        newCode = optimizeMemoryAllocation(code, lang);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Converted heap allocations to stack where possible.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double optimizeMemoryTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("optimizeMemoryAllocation", optimizeMemoryTime));

        // 5. Inline Function Expansion (New)
        startTime = System.nanoTime();
        newCode = inlineFunctions(code, lang);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Inlined small functions to reduce function call overhead.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double inlineFunctionsTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("inlineFunctions", inlineFunctionsTime));

        // 6. Loop Unrolling (New)
        startTime = System.nanoTime();
        newCode = unrollLoops(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Unrolled small loops to reduce loop overhead.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double unrollLoopsTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("unrollLoops", unrollLoopsTime));

        // 7. Common Subexpression Elimination (New)
        startTime = System.nanoTime();
        newCode = eliminateCommonSubexpressions(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Eliminated common subexpressions to avoid redundant computations.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double cseTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("eliminateCommonSubexpressions", cseTime));

        // 8. Strength Reduction (New)
        startTime = System.nanoTime();
        newCode = applyStrengthReduction(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Applied strength reduction to replace expensive operations.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double strengthReductionTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("applyStrengthReduction", strengthReductionTime));

        // 9. Code Hoisting (New)
        startTime = System.nanoTime();
        newCode = hoistCode(code);
        if (!newCode.equals(code)) {
            optimizationInsights.add("Hoisted invariant code outside of loops.");
        }
        code = newCode;
        endTime = System.nanoTime();
        double hoistCodeTime = (endTime - startTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("hoistCode", hoistCodeTime));

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

        Pattern mallocPattern = Pattern.compile("malloc\\s*\\(\\s*(\\d+)\\s*\\*\\s*sizeof\\s*\\(\\s*\\w+\\s*\\)\\s*\\)");
        Matcher mallocMatcher = mallocPattern.matcher(code);
        while (mallocMatcher.find()) {
            heapSize += Long.parseLong(mallocMatcher.group(1)) * 4;
        }

        Pattern newPattern = Pattern.compile("new\\s+\\w+\\s*\\[\\s*(\\d+)\\s*\\]");
        Matcher newMatcher = newPattern.matcher(code);
        while (newMatcher.find()) {
            heapSize += Long.parseLong(newMatcher.group(1)) * 4;
        }

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

        Pattern varPattern = Pattern.compile("(int|float|double|char)\\s+(\\w+)(\\s*=\\s*[^;]+)?;");
        for (String line : lines) {
            Matcher matcher = varPattern.matcher(line);
            if (matcher.find()) {
                variables.add(matcher.group(2));
            }
        }

        for (String line : lines) {
            for (String var : variables) {
                if (line.contains(var) && !line.matches(".*(int|float|double|char)\\s+" + var + "(\\s*=\\s*[^;]+)?;.*")) {
                    usedVariables.add(var);
                }
            }
        }

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
                if (size <= 10) {
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

    // New Optimization: Inline Function Expansion
    private String inlineFunctions(String code, Language lang) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();
        List<String> functionDefs = new ArrayList<>();
        List<String> functionBodies = new ArrayList<>();

        Pattern funcPattern = Pattern.compile("(int|float|double|char)\\s+(\\w+)\\s*\\([^)]*\\)\\s*\\{([^}]*)\\}");
        Matcher funcMatcher = funcPattern.matcher(code);
        while (funcMatcher.find()) {
            String returnType = funcMatcher.group(1);
            String funcName = funcMatcher.group(2);
            String body = funcMatcher.group(3).trim();
            String[] bodyLines = body.split("\n");
            boolean isSmall = bodyLines.length <= 3 && !body.contains("for") && !body.contains("while");
            if (isSmall) {
                functionDefs.add(funcName);
                functionBodies.add(body.trim());
            }
        }

        boolean inlined = false;
        for (String line : lines) {
            String newLine = line;
            for (int i = 0; i < functionDefs.size(); i++) {
                String funcName = functionDefs.get(i);
                String body = functionBodies.get(i);
                Pattern callPattern = Pattern.compile(funcName + "\\s*\\(([^)]*)\\)\\s*;");
                Matcher callMatcher = callPattern.matcher(newLine);
                if (callMatcher.find()) {
                    String args = callMatcher.group(1);
                    String inlinedBody = body;
                    if (!args.isEmpty()) {
                        String param = args.trim();
                        Pattern paramPattern = Pattern.compile("\\b\\w+\\b");
                        Matcher paramMatcher = paramPattern.matcher(body);
                        if (paramMatcher.find()) {
                            String paramName = paramMatcher.group();
                            inlinedBody = inlinedBody.replaceAll("\\b" + paramName + "\\b", param);
                        }
                    }
                    newLine = newLine.replaceAll(funcName + "\\s*\\([^)]*\\)\\s*;", inlinedBody + ";");
                    inlined = true;
                }
            }
            newLines.add(newLine);
        }

        if (inlined) {
            String result = String.join("\n", newLines);
            for (String funcName : functionDefs) {
                result = result.replaceAll("(int|float|double|char)\\s+" + funcName + "\\s*\\([^)]*\\)\\s*\\{[^}]*\\}", "");
            }
            return result;
        }

        return code;
    }

    // New Optimization: Loop Unrolling
    private String unrollLoops(String code) {
        Pattern loopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\1\\s*<\\s*(\\d+)\\s*;\\s*\\1\\s*\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher matcher = loopPattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String loopVar = matcher.group(1);
            int start = Integer.parseInt(matcher.group(2));
            int end = Integer.parseInt(matcher.group(3));
            String body = matcher.group(4).trim();

            int iterations = end - start;
            if (iterations <= 4 && iterations > 0) {
                StringBuilder unrolled = new StringBuilder();
                for (int i = start; i < end; i++) {
                    String unrolledBody = body.replaceAll("\\b" + loopVar + "\\b", String.valueOf(i));
                    unrolled.append(unrolledBody).append("\n");
                }
                matcher.appendReplacement(result, unrolled.toString());
            } else {
                matcher.appendReplacement(result, matcher.group(0));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    // New Optimization: Common Subexpression Elimination
    private String eliminateCommonSubexpressions(String code) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();

        Pattern exprPattern = Pattern.compile("(\\w+)\\s*=\\s*(\\w+\\s*\\*\\s*\\w+);");
        List<String> expressions = new ArrayList<>();
        List<String> tempVars = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = exprPattern.matcher(line);
            if (matcher.find()) {
                String var = matcher.group(1);
                String expr = matcher.group(2);
                int index = expressions.indexOf(expr);
                if (index != -1) {
                    newLines.add(var + " = " + tempVars.get(index) + ";");
                    continue;
                } else {
                    String tempVar = "temp_" + expressions.size();
                    expressions.add(expr);
                    tempVars.add(tempVar);
                    newLines.add("int " + tempVar + " = " + expr + ";");
                    newLines.add(var + " = " + tempVar + ";");
                    continue;
                }
            }
            newLines.add(line);
        }

        return String.join("\n", newLines);
    }

    // New Optimization: Strength Reduction
    private String applyStrengthReduction(String code) {
        Pattern loopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\1\\s*<\\s*(\\d+)\\s*;\\s*\\1\\s*\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher matcher = loopPattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String loopVar = matcher.group(1);
            String body = matcher.group(4).trim();
            Pattern multPattern = Pattern.compile("(\\w+)\\s*=\\s*" + loopVar + "\\s*\\*\\s*(\\d+);");
            Matcher multMatcher = multPattern.matcher(body);
            if (multMatcher.find()) {
                String resultVar = multMatcher.group(1);
                int multiplier = Integer.parseInt(multMatcher.group(2));
                String newBody = "    " + resultVar + " = " + resultVar + " + " + multiplier + ";";
                String init = "int " + resultVar + " = " + (Integer.parseInt(matcher.group(2)) * multiplier) + ";\n";
                String newLoop = init + "for (int " + loopVar + " = " + matcher.group(2) + "; " + loopVar + " < " + matcher.group(3) + "; " + loopVar + "++) {\n" + newBody + "\n}";
                matcher.appendReplacement(result, newLoop);
            } else {
                matcher.appendReplacement(result, matcher.group(0));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    // New Optimization: Code Hoisting
    private String hoistCode(String code) {
        Pattern loopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\1\\s*<\\s*(\\d+)\\s*;\\s*\\1\\s*\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher matcher = loopPattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String loopVar = matcher.group(1);
            String body = matcher.group(4).trim();
            String[] bodyLines = body.split("\n");
            List<String> newBodyLines = new ArrayList<>();
            List<String> hoistedLines = new ArrayList<>();

            for (String line : bodyLines) {
                line = line.trim();
                if (!line.isEmpty() && !line.contains(loopVar) && line.matches(".*=.*;")) {
                    hoistedLines.add(line);
                } else {
                    newBodyLines.add(line);
                }
            }

            String newLoop = "";
            if (!hoistedLines.isEmpty()) {
                newLoop += String.join("\n", hoistedLines) + "\n";
            }
            newLoop += "for (int " + loopVar + " = " + matcher.group(2) + "; " + loopVar + " < " + matcher.group(3) + "; " + loopVar + "++) {\n";
            newLoop += String.join("\n", newBodyLines) + "\n}";
            matcher.appendReplacement(result, newLoop);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}