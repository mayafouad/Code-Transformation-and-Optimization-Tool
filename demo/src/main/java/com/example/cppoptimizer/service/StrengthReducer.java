package com.example.cppoptimizer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrengthReducer extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
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

    @Override
    public String getInsight() {
        return "Applied strength reduction to replace expensive operations.";
    }

    @Override
    public String getName() {
        return "applyStrengthReduction";
    }
}