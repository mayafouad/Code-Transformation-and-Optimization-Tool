
# Automated CTO Tool

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring](https://img.shields.io/badge/Spring-5.3-green) ![License](https://img.shields.io/badge/License-MIT-blue)

The **Automated CTO Tool** is a Spring-based application designed to assist developers and technical leads by providing automated code optimization and analysis for C and C++ codebases. It leverages the `CodeOptimizerService` to enhance code performance, reduce memory usage, and provide actionable insights—acting as a virtual "Chief Technology Officer" for your project.

## Features
- **Code Optimization**: Applies techniques like constant folding, loop optimization, dead code elimination, and memory allocation improvements.
- **Language Support**: Automatically detects and optimizes C and C++ code.
- **Metrics & Insights**:
  - Estimates heap and stack memory usage before and after optimization.
  - Tracks execution time for optimization steps.
  - Provides detailed insights into applied optimizations.

## Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- Spring Boot 2.7+

### Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/mayafouad/Automated-CTO-Tool.git
   cd Automated-CTO-Tool
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

### Inject the Optimizer Service
The core of the tool is the `CodeOptimizerService`, a Spring `@Service` that you can inject into your components:
```java
@Autowired
private CodeOptimizerService optimizerService;
```

### Optimize Code
Pass your C/C++ code as a string to the `optimize` method:
```java
String code = "int main() {\n  int a = 2 + 3;\n  int sum = 0;\n  for (int i = 0; i < 5; i++) { sum += i; }\n  return 0;\n}";
OptimizationResult result = optimizerService.optimize(code);
```

### Access Results
Retrieve the optimized code, memory stats, timing, and insights:
```java
System.out.println("Optimized Code: " + result.getOptimizedCode());
System.out.println("Heap Before: " + result.getBeforeMemory().getHeapSize());
System.out.println("Heap After: " + result.getAfterMemory().getHeapSize());
System.out.println("Insights: " + result.getOptimizationInsights());
```

## Optimization Techniques

### 1. Constant Folding
Simplifies constant arithmetic expressions (e.g., `2 + 3` becomes `5`).

### 2. Arithmetic Loop Optimization
Converts summation loops (e.g., `for (int i = 0; i < 5; i++) { sum += i; }`) into direct assignments (e.g., `sum = 10;`).

### 3. Dead Code Elimination
Removes unused variables to streamline the code.

### 4. Memory Allocation Optimization
Converts small heap allocations (e.g., `malloc` or `new`) to stack arrays for sizes ≤ 10 elements.

## Metrics Provided
- **Memory Usage**: Estimates heap and stack sizes (e.g., `int` = 4 bytes).
- **Timing**: Tracks time (in milliseconds) for each optimization step and total process.
- **Insights**: Lists applied optimizations (e.g., "Optimized arithmetic loop to direct assignment").

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

## Project Structure

### Core Component: `CodeOptimizerService`
- **Enums and Nested Classes**:
  - `Language`: Supports `C` and `CPP`.
  - `MemoryUsage`: Tracks heap and stack sizes.
  - `TimingEntry`: Logs time per optimization step.
  - `OptimizationResult`: Contains optimized code, memory stats, timing, and insights.

- **Main Method**:
  - `optimize(String code)`: Applies optimizations and returns results.

- **Helper Methods**:
  - `detectLanguage`: Identifies C or C++.
  - `estimateMemoryUsage`: Estimates memory usage.
  - `foldConstants`: Folds constants.
  - `optimizeArithmeticLoops`: Optimizes loops.
  - `eliminateDeadCode`: Removes dead code.
  - `optimizeMemoryAllocation`: Optimizes memory usage.

## Limitations
- **Memory Estimation**: Uses fixed sizes (e.g., `int` = 4 bytes) and may not reflect platform-specific variations.
- **Pattern Matching**: Relies on regex, potentially missing complex code structures.
- **Allocation Threshold**: Heap-to-stack conversion is capped at 10 elements.

## Dependencies
- **Spring Framework**: For `@Service` and dependency injection.
- **SLF4J**: For logging optimization steps.

## Extending the Tool
- Add new optimization techniques in `optimize`.
- Enhance `estimateMemoryUsage` for platform-specific accuracy.
- Improve regex patterns to handle more code patterns.

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For questions or feedback, open an issue or reach out via GitHub: [mayafouad](https://github.com/mayafouad).

