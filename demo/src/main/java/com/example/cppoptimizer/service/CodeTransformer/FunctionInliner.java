package com.example.cppoptimizer.service.CodeTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cppoptimizer.service.CodeOptimizerService;

public class FunctionInliner extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        // Normalize line endings
        code = code.replaceAll("\r\n", "\n").replaceAll("\r", "\n");

        List<String> newLines = new ArrayList<>();
        List<String> functionDefs = new ArrayList<>();
        List<String> functionBodies = new ArrayList<>();
        List<String> functionParams = new ArrayList<>();

        // Pattern to match function definitions
        Pattern funcPattern = Pattern.compile(
            "(int|float|double|char)\\s+(\\w+)\\s*\\(\\s*(\\w+\\s+\\w+)\\s*\\)\\s*\\{([^}]*)\\}",
            Pattern.DOTALL
        );
        Matcher funcMatcher = funcPattern.matcher(code);
        while (funcMatcher.find()) {
            String funcName = funcMatcher.group(2);
            String param = funcMatcher.group(3).split("\\s+")[1]; // e.g., "int x" -> "x"
            String body = funcMatcher.group(4).trim();
            String[] bodyLines = body.split("\n");
            boolean isSmall = bodyLines.length <= 3 && !body.contains("for") && !body.contains("while");
            if (isSmall) {
                functionDefs.add(funcName);
                functionBodies.add(body);
                functionParams.add(param);
            }
        }

        boolean inlined = false;
        String[] lines = code.split("\n");
        for (String line : lines) {
            String newLine = line;
            for (int i = 0; i < functionDefs.size(); i++) {
                String funcName = functionDefs.get(i);
                String body = functionBodies.get(i);
                String param = functionParams.get(i);
                Pattern callPattern = Pattern.compile(funcName + "\\s*\\(\\s*([^)]+)\\s*\\)\\s*;");
                Matcher callMatcher = callPattern.matcher(newLine);
                if (callMatcher.find()) {
                    String arg = callMatcher.group(1).trim();

                    // Substitute parameter in the body
                    String inlinedBody = body.replaceAll("\\b" + param + "\\b", arg);

                    // Handle return statements
                    Pattern returnPattern = Pattern.compile("return\\s+([^;]+);", Pattern.DOTALL);
                    Matcher returnMatcher = returnPattern.matcher(inlinedBody);
                    if (returnMatcher.find()) {
                        inlinedBody = returnMatcher.group(1).trim(); // Extract expression after 'return'

                        // Try to evaluate constant expressions
                        Pattern constPattern = Pattern.compile("(\\d+)\\s*\\+\\s*(\\d+)");
                        Matcher constMatcher = constPattern.matcher(inlinedBody);
                        if (constMatcher.matches()) {
                            int val = Integer.parseInt(constMatcher.group(1)) + Integer.parseInt(constMatcher.group(2));
                            inlinedBody = String.valueOf(val);
                        }
                    } else {
                        // If no return statement, use the entire body (e.g., for void functions or implicit returns)
                        inlinedBody = inlinedBody.trim();
                    }

                    // Replace the function call with the inlined body
                    newLine = newLine.replaceAll(funcName + "\\s*\\([^)]*\\)\\s*;", inlinedBody + ";");
                    inlined = true;
                }
            }
            newLines.add(newLine);
        }

        if (inlined) {
            String result = String.join("\n", newLines);
            // Remove original function definitions
            for (String funcName : functionDefs) {
                result = result.replaceAll(
                    "(int|float|double|char)\\s+" + funcName + "\\s*\\([^)]*\\)\\s*\\{[^}]*\\}",
                    ""
                );
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