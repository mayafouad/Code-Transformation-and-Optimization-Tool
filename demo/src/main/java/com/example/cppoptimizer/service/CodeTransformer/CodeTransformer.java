package com.example.cppoptimizer.service.CodeTransformer;
import com.example.cppoptimizer.service.CodeOptimizerService;

public abstract class CodeTransformer {
    public abstract String transform(String code, CodeOptimizerService.Language lang);
    public abstract String getInsight();
    public abstract String getName();
}