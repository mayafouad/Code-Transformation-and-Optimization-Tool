# Automated Code Transformation and Optimization Tool

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring](https://img.shields.io/badge/Spring-5.3-green) ![License](https://img.shields.io/badge/License-MIT-blue)

Welcome to the **C/C++ Code Optimizer**, a Spring-based service designed to optimize C and C++ code by improving performance and memory usage. This tool analyzes input code, applies various optimization techniques, and provides detailed metrics such as memory usage, timing, and optimization insights.

## Features
- **Language Detection**: Automatically identifies C or C++ code.
- **Optimizations**:
  - Constant folding for arithmetic expressions.
  - Arithmetic loop optimization (e.g., summation loops to formulas).
  - Dead code elimination (removes unused variables).
  - Memory allocation optimization (heap to stack conversion for small allocations).
- **Metrics**:
  - Estimates heap and stack memory usage before and after optimization.
  - Tracks execution time for each optimization step.
  - Provides insights into applied optimizations.

## Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- Spring Boot 2.7+ (for running as a Spring application)

### Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/cpp-optimizer.git
   cd cpp-optimizer
   ```

2. **Build the Project**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

## Usage

### Inject the Service
Since `CodeOptimizerService` is a Spring `@Service`, you can inject it into your components:
```java
@Autowired
private CodeOptimizerService optimizerService;
```

### Optimize Code
Pass a string containing C/C++ code to the `optimize` method:
```java
String code = "int main() {\n  int a = 2 + 3;\n  int sum = 0;\n  for (int i = 0; i < 5; i++) { sum += i; }\n  return 0;\n}";
OptimizationResult result = optimizerService.optimize(code);
```

### Access Results
Retrieve the optimized code, memory usage, timing, and insights:
```java
System.out.println("Optimized Code: " + result.getOptimizedCode());
System.out.println("Heap Before: " + result.getBeforeMemory().getHeapSize());
System.out.println("Heap After: " + result.getAfterMemory().getHeapSize());
System.out.println("Insights: " + result.getOptimizationInsights());
```

## Optimization Techniques

### 1. Constant Folding
Simplifies arithmetic expressions with constants (e.g., `2 + 3` becomes `5`) using regex-based pattern matching.

### 2. Arithmetic Loop Optimization
Replaces loops like `for (int i = 0; i < n; i++) { sum += i; }` with a direct formula (e.g., `sum = (n * (n-1)) / 2;`).

### 3. Dead Code Elimination
Removes unused variable declarations by analyzing variable usage.

### 4. Memory Allocation Optimization
Converts small heap allocations (e.g., `malloc` or `new`) to stack arrays when the size is ≤ 10 elements.

## Metrics Provided
- **Memory Usage**: Estimates heap and stack sizes (e.g., `int` = 4 bytes).
- **Timing**: Measures time (in milliseconds) for each optimization step and total execution.
- **Insights**: Lists applied optimizations (e.g., "Applied constant folding to simplify arithmetic expressions").

## Example

### Input Code
```cpp
int main() {
    int a = 2 + 3;
    int sum = 0;
    for (int i = 0; i < 5; i++) { sum += i; }
    int *arr = (int*)malloc(5 * sizeof(int));
    free(arr);
    return 0;
}
```

### Output
- **Optimized Code**:
  ```cpp
  int main() {
      int a = 5;
      int sum = 10;
      int arr[5];
      return 0;
  }
  ```
- **Insights**:
  - "Applied constant folding to simplify arithmetic expressions."
  - "Optimized arithmetic loop to direct assignment."
  - "Converted heap allocations to stack where possible."
- **Memory Usage**:
  - Before: Heap = 20 bytes, Stack = 4 bytes
  - After: Heap = 0 bytes, Stack = 24 bytes

## Class Structure

### Main Components
- **Enums and Nested Classes**:
  - `Language`: Enum for `C` and `CPP`.
  - `MemoryUsage`: Tracks heap and stack sizes.
  - `TimingEntry`: Records time per optimization step.
  - `OptimizationResult`: Contains optimized code, memory stats, timing, and insights.

- **Main Method**:
  - `optimize(String code)`: Applies optimizations and returns results.

- **Helper Methods**:
  - `detectLanguage`: Detects C or C++.
  - `estimateMemoryUsage`: Estimates memory usage.
  - `foldConstants`: Folds constants.
  - `optimizeArithmeticLoops`: Optimizes loops.
  - `eliminateDeadCode`: Removes dead code.
  - `optimizeMemoryAllocation`: Optimizes memory usage.

## Limitations
- **Memory Estimation**: Assumes fixed sizes (e.g., `int` = 4 bytes) and ignores platform-specific details.
- **Pattern Matching**: Uses regex, which may miss complex patterns.
- **Allocation Threshold**: Heap-to-stack conversion is limited to sizes ≤ 10.

## Dependencies
- **Spring Framework**: For `@Service` and dependency injection.
- **SLF4J**: For logging.

## How to Extend
- Add new optimization methods in `optimize`.
- Improve `estimateMemoryUsage` for better accuracy.
- Enhance regex patterns for broader code coverage.

## Contributing
We welcome contributions! To get started:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For questions or suggestions, feel free to open an issue or reach out via GitHub.

