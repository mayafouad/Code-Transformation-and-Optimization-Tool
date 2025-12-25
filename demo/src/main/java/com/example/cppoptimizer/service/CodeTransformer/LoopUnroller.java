package com.example.cppoptimizer.service.CodeTransformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cppoptimizer.service.CodeOptimizerService;

public class LoopUnroller extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        Pattern loopPattern = Pattern.compile("for\\s*\\(\\s*int\\s+(\\w+)\\s*=\\s*(\\d+)\\s*;\\s*\\1\\s*<\\s*(\\d+)\\s*;\\s*\\1\\s*\\+\\+\\s*\\)\\s*\\{([^}]*)\\}");
        Matcher matcher = loopPattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String loopVar = matcher.group(1);
            int start = Integer.parseInt(matcher.group(2));
            int end = Integer.parseInt(matcher.group(3));
            String body = matcher.group(4).trim();

            int iterations = end - start;
            if (iterations <= 4 && iterations > 0) {
                StringBuilder unrolled = new StringBuilder();
                for (int i = start; i < end; i++) {
                    String unrolledBody = body.replaceAll("\\b" + loopVar + "\\b", String.valueOf(i));
                    unrolled.append(unrolledBody).append("\n");
                }
                matcher.appendReplacement(result, unrolled.toString());
            } else {
                matcher.appendReplacement(result, matcher.group(0));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    @Override
    public String getInsight() {
        return "Unrolled small loops to reduce loop overhead.";
    }

    @Override
    public String getName() {
        return "unrollLoops";
    }
}