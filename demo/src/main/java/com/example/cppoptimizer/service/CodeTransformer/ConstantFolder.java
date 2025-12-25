package com.example.cppoptimizer.service.CodeTransformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cppoptimizer.service.CodeOptimizerService;

public class ConstantFolder extends CodeTransformer {
    @Override
    public String transform(String code, CodeOptimizerService.Language lang) {
        Pattern pattern = Pattern.compile("(\\d+)\\s*\\+\\s*(\\d+)");
        Matcher matcher = pattern.matcher(code);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));
            matcher.appendReplacement(result, String.valueOf(a + b));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    public String getInsight() {
        return "Applied constant folding to simplify arithmetic expressions.";
    }

    @Override
    public String getName() {
        return "foldConstants";
    }
}