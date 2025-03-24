package com.example.cppoptimizer.service;

public class LanguageDetector {
    public CodeOptimizerService.Language detectLanguage(String code) {
        if (code.contains("iostream") || code.contains("using namespace std;") || 
            code.contains("cout") || code.contains("cin")) {
            return CodeOptimizerService.Language.CPP;
        }
        return CodeOptimizerService.Language.C;
    }
}