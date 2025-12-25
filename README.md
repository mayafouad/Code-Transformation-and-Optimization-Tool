# ⚡CTO Tool

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring](https://img.shields.io/badge/Spring-5.3-green) 

The **CTO -Code Transformation and Optimization- Tool** is a web-based application designed to assist developers and technical leads by providing automated code optimization and analysis for C and C++ codes. It features a frontend built with JavaScript and CSS, running on localhost, that accepts file input (`.c` or `.cpp` file). The project is structured into modular classes, each responsible for a specific optimization, following the Single Responsibility Principle (SRP). The main entry point is the `CodeOptimizerService`, which orchestrates the optimization process by applying a series of transformations to the input code.

## ✨Features
- **Web Interface**: Built with JavaScript and CSS, runs on localhost, and supports file input for C/C++ code.
- **Code Optimization**: Applies constant folding, loop optimization, dead code elimination, and memory allocation improvements.
- **Language Support**: Automatically detects and optimizes C and C++ code.
- **Metrics & Insights**:
  - Estimates heap and stack memory usage before and after optimization.
  - Tracks execution time for optimization steps.
  - Provides detailed insights into applied optimizations.

## ⬇️Installation

### Prerequisites:
- **Backend**: Java 17+, Maven 3.6+, Spring Boot 2.7+
- **Frontend**: A modern web browser (no additional setup required beyond serving the files)

### Steps:
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

## 🚀Usage

### Via the Web Interface:
1. **Upload a File**: Use the file input field to upload a `.c` or `.cpp` file containing your code.
2. **Optimize**: Click the "Optimize" button to process the file.
3. **View Results**: The optimized code, memory usage, timing, and insights will be displayed on the page.

### Backend Usage (For Developers):

### - Using the CodeOptimizerService Directly
Inject the `CodeOptimizerService` into your Spring components:
```java
@Autowired
private CodeOptimizerService optimizerService;

String code = "int main() { int a = 2 + 3; return 0; }";
OptimizationResult result = optimizerService.optimize(code);
System.out.println("Optimized Code: " + result.getOptimizedCode());
```
### - Using the API Endpoint
You can also optimize code by sending a file via the `/optimize` API endpoint. This endpoint accepts a multipart file upload and returns the optimization results, which can be rendered in the web interface or processed further.

#### Endpoint Details
- **URL**: `/optimize`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Request Parameter**:
  - `file`: The C/C++ source file to optimize (e.g., a `.c` or `.cpp` file).
- **Response**:
  - Renders the `index` view with the following model attributes:
    - `originalCode`: The original code from the uploaded file.
    - `optimizedCode`: The optimized code.
    - `beforeMemory`: Memory usage before optimization.
    - `afterMemory`: Memory usage after optimization.
    - `timingEntries`: Timing information for each optimization step.
    - `optimizationInsights`: A list of insights describing the applied optimizations.

#### Example Usage with a REST Client (e.g., Postman)
1. Open Postman or your preferred REST client.
2. Set the request method to `POST`.
3. Set the URL to `http://localhost:8080/optimize`.
4. In the "Body" tab, select `form-data`.
5. Add a key named `file`, set its type to `File`, and upload your C/C++ source file (e.g., `code.cpp`).
6. Send the request.
7. The response will be an HTML page (`index.html`) containing the optimization results, including the original and optimized code, memory usage, timing, and insights.

   
## 📈Optimization Techniques

### 1. Constant Folding (`ConstantFolder`)
Constant folding simplifies arithmetic expressions involving constants at compile time, reducing runtime computation.

### 2. Arithmetic Loop Optimization (`ArithmeticLoopOptimizer`)
Optimizes loops that perform arithmetic accumulation (e.g., summing numbers) by replacing them with direct assignments using closed-form solutions.

### 3. Dead Code Elimination (`DeadCodeEliminator`)
Removes variables that are declared but never used, reducing unnecessary memory usage and improving code clarity.

### 4. Memory Allocation Optimization (`MemoryAllocationOptimizer`)
Converts small heap allocations (e.g., `malloc` or `new`) to stack allocations when possible, reducing memory management overhead.

### 5. Function Inlining (`FunctionInliner`)
Replaces calls to small functions with their bodies, eliminating function call overhead.

### 6. Loop Unrolling (`LoopUnroller`)
Expands small loops into repeated statements, reducing loop control overhead.

### 7. Common Subexpression Elimination (`CommonSubexpressionEliminator`)
Removes redundant computations by storing results in temporary variables and reusing them.

### 9. Code Hoisting (`CodeHoister`)
Moves loop-invariant code (expressions independent of the loop variable) outside the loop to avoid redundant execution.


## 📊Metrics Provided
- **Memory Usage**: Estimates heap and stack sizes (e.g., `int` = 4 bytes).
- **Timing**: Tracks time (in milliseconds) for each optimization step and total process.
- **Insights**: Lists applied optimizations (e.g., "Optimized arithmetic loop to direct assignment").

## 🧩Project Structure

### Backend: `CodeOptimizerService`
- **Location**: `backend/src/main/java/com/example/cppoptimizer/service/CodeOptimizerService.java`
- **`CodeOptimizerService`**: Main service class that coordinates the optimization process.
- **`LanguageDetector`**: Detects whether the input code is C or C++.
- **`MemoryAnalyzer`**: Estimates heap and stack memory usage.
- **`CodeTransformer`**: Abstract base class for all optimization transformers.
- **Optimization Transformers**: Individual classes for each optimization technique:
  - `ConstantFolder`
  - `ArithmeticLoopOptimizer`
  - `DeadCodeEliminator`
  - `MemoryAllocationOptimizer`
  - `FunctionInliner`
  - `LoopUnroller`
  - `CommonSubexpressionEliminator`
  - `StrengthReducer`
  - `CodeHoister`

## 🟣Dependencies
- **Backend**:
  - Spring Framework (for `@Service` and dependency injection)
  - Log4j (for logging)
  - ANTLR (for parsing C/C++)
    
 ## 🟢Tutorial
https://github.com/user-attachments/assets/1a691249-b80d-4919-a44b-363f4b28b90f

## 🤝Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.


## 📧Contact
For questions or feedback, open an issue or reach out via GitHub: [mayafouad](https://github.com/mayafouad).


