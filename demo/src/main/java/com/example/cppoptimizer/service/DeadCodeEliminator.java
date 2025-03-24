package com.example.cppoptimizer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeadCodeEliminator extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
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

    @Override
    public String getInsight() {
        return "Eliminated unused variables (dead code).";
    }

    @Override
    public String getName() {
        return "eliminateDeadCode";
    }
}