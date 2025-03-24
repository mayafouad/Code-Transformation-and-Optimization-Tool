# ⚡Automated CTO Tool

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring](https://img.shields.io/badge/Spring-5.3-green) 

The **Automated CTO Tool** is a web-based application designed to assist developers and technical leads by providing automated code optimization and analysis for C and C++ codebases. It features a frontend built with JavaScript and CSS, running on localhost, that accepts file input (e.g., `.c` or `.cpp` files) and processes it using a Spring Boot backend powered by the `CodeOptimizerService`. This tool acts as a virtual "Chief Technology Officer" to enhance code performance, reduce memory usage, and deliver actionable insights.

## Features
- **Web Interface**: Built with JavaScript and CSS, runs on localhost, and supports file input for C/C++ code.
- **Code Optimization**: Applies constant folding, loop optimization, dead code elimination, and memory allocation improvements.
- **Language Support**: Automatically detects and optimizes C and C++ code.
- **Metrics & Insights**:
  - Estimates heap and stack memory usage before and after optimization.
  - Tracks execution time for optimization steps.
  - Provides detailed insights into applied optimizations.

## Installation

### Prerequisites
- **Backend**: Java 17+, Maven 3.6+, Spring Boot 2.7+
- **Frontend**: A modern web browser (no additional setup required beyond serving the files)

### Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/mayafouad/Automated-CTO-Tool.git
   cd Automated-CTO-Tool
   ```

2. **Build and Run the Backend**:
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```
   - The backend runs on `http://localhost:8080`.

4. **Access the Tool**:
   - Open `http://localhost:8080` in your browser.

## Usage

### Via the Web Interface
1. **Upload a File**: Use the file input field to upload a `.c` or `.cpp` file containing your code.
2. **Optimize**: Click the "Optimize" button to process the file.
3. **View Results**: The optimized code, memory usage, timing, and insights will be displayed on the page.

### Backend Usage (For Developers)
Inject the `CodeOptimizerService` into your Spring components:
```java
@Autowired
private CodeOptimizerService optimizerService;

String code = "int main() { int a = 2 + 3; return 0; }";
OptimizationResult result = optimizerService.optimize(code);
System.out.println("Optimized Code: " + result.getOptimizedCode());
```

## Optimization Techniques

### 1. Constant Folding
Simplifies constant arithmetic expressions (e.g., `2 + 3` becomes `5`).

### 2. Arithmetic Loop Optimization
Converts summation loops (e.g., `for (int i = 0; i < 5; i++) { sum += i; }`) to direct assignments (e.g., `sum = 10;`).

### 3. Dead Code Elimination
Removes unused variables to streamline the code.

### 4. Memory Allocation Optimization
Converts small heap allocations (e.g., `malloc` or `new`) to stack arrays for sizes ≤ 10 elements.

### 5. Inline Function Expansion
Replaces calls to small functions with the function's body to eliminate function call overhead.
   - Example:
     ```c
     int square(int x) { return x * x; }
     int result = square(5);

## Metrics Provided
- **Memory Usage**: Estimates heap and stack sizes (e.g., `int` = 4 bytes).
- **Timing**: Tracks time (in milliseconds) for each optimization step and total process.
- **Insights**: Lists applied optimizations (e.g., "Optimized arithmetic loop to direct assignment").

## Example

### Input File (`example.c`)
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

### Output (Displayed on Web Interface)
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

### Backend: `CodeOptimizerService`
- **Location**: `backend/src/main/java/com/example/cppoptimizer/service/CodeOptimizerService.java`
- **Enums and Nested Classes**:
  - `Language`: Supports `C` and `CPP`.
  - `MemoryUsage`: Tracks heap and stack sizes.
  - `TimingEntry`: Logs time per optimization step.
  - `OptimizationResult`: Contains optimized code, memory stats, timing, and insights.
- **Main Method**:
  - `optimize(String code)`: Processes the code and returns results.
- **Helper Methods**: `detectLanguage`, `estimateMemoryUsage`, `foldConstants`, etc.


## Dependencies
- **Backend**:
  - Spring Framework (for `@Service` and dependency injection)
  - SLF4J (for logging)

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.


## Contact
For questions or feedback, open an issue or reach out via GitHub: [mayafouad](https://github.com/mayafouad).

