package com.example.cppoptimizer.service.CodeTransformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cppoptimizer.service.CodeOptimizerService;

public class MemoryAllocationOptimizer extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();
        Set<String> convertedArrays = new HashSet<>();

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
                    convertedArrays.add(varName);
                } else {
                    newLines.add(line);
                }
            } else if (newMatcher.find()) {
                String varName = newMatcher.group(1);
                int size = Integer.parseInt(newMatcher.group(2));
                if (size <= 10) {
                    newLines.add("int " + varName + "[" + size + "];");
                    convertedArrays.add(varName);
                } else {
                    newLines.add(line);
                }
            } else if (!convertedArrays.contains(line.trim().replaceAll("delete\\s*\\[\\s*\\]\\s*(\\w+)\\s*;", "$1"))) {
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