package com.example.cppoptimizer.service.CodeTransformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cppoptimizer.service.CodeOptimizerService;

public class ArithmeticLoopOptimizer extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        // Pattern to match the loop, allowing for multi-line structure
        Pattern pattern = Pattern.compile(
            "int\\s+sum\\s*=\\s*0\\s*;\\s*" +
            "for\\s*\\(\\s*int\\s+i\\s*=\\s*0\\s*;\\s*i\\s*<\\s*(\\d+)\\s*;\\s*i\\s*\\+\\+\\s*\\)\\s*\\{\\s*" +
            "sum\\s*\\+=\\s*i\\s*;\\s*\\}",
            Pattern.DOTALL // Allow . to match newlines
        );
        Matcher matcher = pattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int n = Integer.parseInt(matcher.group(1));
            int sum = (n * (n - 1)) / 2;
            matcher.appendReplacement(result, "int sum = " + sum + ";");
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