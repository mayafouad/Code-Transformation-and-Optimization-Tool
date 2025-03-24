package com.example.cppoptimizer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoryAnalyzer {
    public CodeOptimizerService.MemoryUsage estimateMemoryUsage(String code, CodeOptimizerService.Language lang) {
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
                case "int": stackSize += 4; break;
                case "float": stackSize += 4; break;
                case "double": stackSize += 8; break;
                case "char": stackSize += 1; break;
            }
        }

        return new CodeOptimizerService.MemoryUsage(heapSize, stackSize);
    }
}