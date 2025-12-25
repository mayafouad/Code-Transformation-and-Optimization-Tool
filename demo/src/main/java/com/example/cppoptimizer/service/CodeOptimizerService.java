package com.example.cppoptimizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.example.cppoptimizer.service.CodeTransformer.CodeTransformer;
import com.example.cppoptimizer.service.CodeTransformer.ConstantFolder;
import com.example.cppoptimizer.service.CodeTransformer.ArithmeticLoopOptimizer;
import com.example.cppoptimizer.service.CodeTransformer.DeadCodeEliminator;
import com.example.cppoptimizer.service.CodeTransformer.MemoryAllocationOptimizer;
import com.example.cppoptimizer.service.CodeTransformer.FunctionInliner;
import com.example.cppoptimizer.service.CodeTransformer.LoopUnroller;
import com.example.cppoptimizer.service.CodeTransformer.CommonSubexpressionEliminator;
import com.example.cppoptimizer.service.CodeTransformer.CodeHoister;

@Service
public class CodeOptimizerService {
    private static final Logger logger = LoggerFactory.getLogger(CodeOptimizerService.class);

    private final LanguageDetector languageDetector;
    private final MemoryAnalyzer memoryAnalyzer;
    private final List<CodeTransformer> transformers;

    public CodeOptimizerService() {
        this.languageDetector = new LanguageDetector();
        this.memoryAnalyzer = new MemoryAnalyzer();
        this.transformers = initializeTransformers();
    }

    public OptimizationResult optimize(String code) {
        long startTotalTime = System.nanoTime();
        List<TimingEntry> timingEntries = new ArrayList<>();
        List<String> optimizationInsights = new ArrayList<>();

        Language lang = languageDetector.detectLanguage(code);
        logger.info("Detected language: {}", lang);

        MemoryUsage beforeMemory = memoryAnalyzer.estimateMemoryUsage(code, lang);
        String optimizedCode = code;

        for (CodeTransformer transformer : transformers) {
            long startTime = System.nanoTime();
            String newCode = transformer.transform(optimizedCode, lang);
            if (!newCode.equals(optimizedCode)) {
                optimizationInsights.add(transformer.getInsight());
            }
            optimizedCode = newCode;
            double timeMs = (System.nanoTime() - startTime) / 1_000_000.0;
            timingEntries.add(new TimingEntry(transformer.getName(), timeMs));
        }

        MemoryUsage afterMemory = memoryAnalyzer.estimateMemoryUsage(optimizedCode, lang);
        double totalTime = (System.nanoTime() - startTotalTime) / 1_000_000.0;
        timingEntries.add(new TimingEntry("Total", totalTime));

        return new OptimizationResult(optimizedCode, beforeMemory, afterMemory, timingEntries, optimizationInsights);
    }

    private List<CodeTransformer> initializeTransformers() {
        List<CodeTransformer> transformers = new ArrayList<>();
        transformers.add(new ConstantFolder());
        transformers.add(new ArithmeticLoopOptimizer());
        transformers.add(new DeadCodeEliminator());
        transformers.add(new MemoryAllocationOptimizer());
        transformers.add(new FunctionInliner());
        transformers.add(new LoopUnroller());
        transformers.add(new CommonSubexpressionEliminator());
        transformers.add(new CodeHoister());
        return transformers;
    }

    public enum Language {
        C, CPP
    }

    public static class MemoryUsage {
        private long heapSize;
        private long stackSize;

        public MemoryUsage(long heapSize, long stackSize) {
            this.heapSize = heapSize;
            this.stackSize = stackSize;
        }

        public long getHeapSize() { return heapSize; }
        public long getStackSize() { return stackSize; }
    }

    public static class TimingEntry {
        private String step;
        private double timeMs;

        public TimingEntry(String step, double timeMs) {
            this.step = step;
            this.timeMs = timeMs;
        }

        public String getStep() { return step; }
        public double getTimeMs() { return timeMs; }
    }

    public static class OptimizationResult {
        private String optimizedCode;
        private MemoryUsage beforeMemory;
        private MemoryUsage afterMemory;
        private List<TimingEntry> timingEntries;
        private List<String> optimizationInsights;

        public OptimizationResult(String optimizedCode, MemoryUsage beforeMemory, MemoryUsage afterMemory,
                                  List<TimingEntry> timingEntries, List<String> optimizationInsights) {
            this.optimizedCode = optimizedCode;
            this.beforeMemory = beforeMemory;
            this.afterMemory = afterMemory;
            this.timingEntries = timingEntries;
            this.optimizationInsights = optimizationInsights;
        }

        public String getOptimizedCode() { return optimizedCode; }
        public MemoryUsage getBeforeMemory() { return beforeMemory; }
        public MemoryUsage getAfterMemory() { return afterMemory; }
        public List<TimingEntry> getTimingEntries() { return timingEntries; }
        public List<String> getOptimizationInsights() { return optimizationInsights; }
    }
}