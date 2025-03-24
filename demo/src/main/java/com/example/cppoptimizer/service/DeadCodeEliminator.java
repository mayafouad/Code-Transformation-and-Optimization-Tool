package com.example.cppoptimizer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadCodeEliminator extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();
        Map<String, Integer> variableUsageCount = new HashMap<>();

        // Step 1: Count occurrences of each variable
        Pattern varPattern = Pattern.compile("(int|float|double|char)\\s+(\\w+)\\s*(=\\s*[^;]+)?\\s*;");
        for (String line : lines) {
            Matcher matcher = varPattern.matcher(line.trim());
            if (matcher.find()) {
                String varName = matcher.group(2);
                variableUsageCount.put(varName, variableUsageCount.getOrDefault(varName, 0) + 1);
            }
            // Count usages outside declarations
            for (String word : line.split("\\W+")) {
                if (variableUsageCount.containsKey(word) && !line.matches(".*(int|float|double|char)\\s+" + word + "\\s*(=\\s*[^;]+)?\\s*;.*")) {
                    variableUsageCount.put(word, variableUsageCount.get(word) + 1);
                }
            }
        }

        // Step 2: Keep only lines with variables used more than once or non-declaration lines
        for (String line : lines) {
            Matcher matcher = varPattern.matcher(line.trim());
            if (matcher.find()) {
                String varName = matcher.group(2);
                if (variableUsageCount.get(varName) > 1) { // Used more than once (declaration + at least one use)
                    newLines.add(line);
                }
            } else {
                newLines.add(line); // Keep non-declaration lines
            }
        }

        return String.join("\n", newLines);
    }

    @Override
    public String getInsight() {
        return "Eliminated variables declared but not used elsewhere (dead code).";
    }

    @Override
    public String getName() {
        return "eliminateDeadCode";
    }
}