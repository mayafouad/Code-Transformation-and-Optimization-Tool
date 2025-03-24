package com.example.cppoptimizer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoryAllocationOptimizer extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
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

    @Override
    public String getInsight() {
        return "Converted heap allocations to stack where possible.";
    }

    @Override
    public String getName() {
        return "optimizeMemoryAllocation";
    }
}