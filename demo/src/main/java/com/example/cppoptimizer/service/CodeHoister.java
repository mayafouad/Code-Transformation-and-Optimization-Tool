package com.example.cppoptimizer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeHoister extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        Pattern loopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\1\\s*<\\s*(\\d+)\\s*;\\s*\\1\\s*\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher matcher = loopPattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String loopVar = matcher.group(1);
            String body = matcher.group(4).trim();
            String[] bodyLines = body.split("\n");
            List<String> newBodyLines = new ArrayList<>();
            List<String> hoistedLines = new ArrayList<>();

            for (String line : bodyLines) {
                line = line.trim();
                if (!line.isEmpty() && !line.contains(loopVar) && line.matches(".*=.*;")) {
                    hoistedLines.add(line);
                } else {
                    newBodyLines.add("    " + line);
                }
            }

            String newLoop = "";
            if (!hoistedLines.isEmpty()) {
                newLoop += String.join("\n", hoistedLines) + "\n";
            }
            newLoop += "for (int " + loopVar + " = " + matcher.group(2) + "; " + loopVar + " < " + matcher.group(3) + "; " + loopVar + "++) {\n";
            newLoop += String.join("\n", newBodyLines) + "\n}";
            matcher.appendReplacement(result, newLoop);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    @Override
    public String getInsight() {
        return "Hoisted invariant code outside of loops.";
    }

    @Override
    public String getName() {
        return "hoistCode";
    }
}