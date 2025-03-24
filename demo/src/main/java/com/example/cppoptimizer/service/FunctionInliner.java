package com.example.cppoptimizer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionInliner extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
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

    @Override
    public String getInsight() {
        return "Inlined small functions to reduce function call overhead.";
    }

    @Override
    public String getName() {
        return "inlineFunctions";
    }
}