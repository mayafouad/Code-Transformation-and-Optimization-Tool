package com.example.cppoptimizer.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArithmeticLoopOptimizer extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        Pattern pattern = Pattern.compile("for\\s*\\(\\s*int\\s+i\\s*=\\s*0\\s*;\\s*i\\s*<\\s*(\\d+)\\s*;\\s*i\\s*\\+\\+\\s*\\)\\s*\\{\\s*sum\\s*\\+=\\s*i\\s*;\\s*\\}");
        Matcher matcher = pattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int n = Integer.parseInt(matcher.group(1));
            int sum = (n * (n - 1)) / 2;
            matcher.appendReplacement(result, "sum = " + sum + ";");
        }
        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    public String getInsight() {
        return "Optimized arithmetic loop to direct assignment.";
    }

    @Override
    public String getName() {
        return "optimizeArithmeticLoops";
    }
}