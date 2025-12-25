package com.example.cppoptimizer.service.CodeTransformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cppoptimizer.service.CodeOptimizerService;

public class CommonSubexpressionEliminator extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        String[] lines = code.split("\n");
        List<String> newLines = new ArrayList<>();
        Set<String> declaredVars = new HashSet<>();

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
                    if (!declaredVars.contains(var)) {
                        newLines.add("int " + var + " = " + tempVars.get(index) + ";");
                        declaredVars.add(var);
                    } else {
                        newLines.add(var + " = " + tempVars.get(index) + ";");
                    }
                } else {
                    String tempVar = "temp_" + expressions.size();
                    expressions.add(expr);
                    tempVars.add(tempVar);
                    newLines.add("int " + tempVar + " = " + expr + ";");
                    if (!declaredVars.contains(var)) {
                        newLines.add("int " + var + " = " + tempVar + ";");
                        declaredVars.add(var);
                    } else {
                        newLines.add(var + " = " + tempVar + ";");
                    }
                }
            } else {
                newLines.add(line);
                Pattern varDecl = Pattern.compile("(int|float|double|char)\\s+(\\w+)\\s*(=.*)?;");
                Matcher declMatcher = varDecl.matcher(line);
                if (declMatcher.find()) {
                    declaredVars.add(declMatcher.group(2));
                }
            }
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