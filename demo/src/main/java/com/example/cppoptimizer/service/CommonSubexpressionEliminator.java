package com.example.cppoptimizer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonSubexpressionEliminator extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
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

    @Override
    public String getInsight() {
        return "Eliminated common subexpressions to avoid redundant computations.";
    }

    @Override
    public String getName() {
        return "eliminateCommonSubexpressions";
    }
}