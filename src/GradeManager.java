import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * GradeManager for Fold 3 - Manages all grades in the system.
 * Uses optimized collections for better performance.
 * Beginner-friendly version with clear explanations.
 */
public class GradeManager {

    // ========== MAIN COLLECTIONS ==========
    // These are like different filing systems for our grades

    // 1. Main storage - All grades by their unique ID
    // ConcurrentHashMap is thread-safe and fast for lookups
    private final Map<String, Grade> allGrades;

    // 2. Index by student - Quick access to a student's grades
    // Key: studentId, Value: List of that student's grades
    private final Map<String, List<Grade>> gradesByStudent;

    // 3. Index by subject - Quick access to subject grades
    // Key: subjectCode, Value: List of grades for that subject
    private final Map<String, List<Grade>> gradesBySubject;

    // ========== SIMPLE CACHE ==========
    // Cache stores recent calculations to avoid re-calculating
    private static class SimpleCache {
        String key;                // What we calculated (e.g., "avg:STU001")
        double value;              // The calculated value
        long timestamp;            // When we calculated it

        SimpleCache(String key, double value) {
            this.key = key;
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }

        // Check if cache is still fresh (less than 30 seconds old)
        boolean isFresh() {
            return System.currentTimeMillis() - timestamp < 30000;
        }
    }

    // Store our cache entries
    private final Map<String, SimpleCache> calculationCache = new HashMap<>();

    // ========== PERFORMANCE TRACKING ==========
    private int totalGradesAdded = 0;
    private int totalCalculations = 0;
    private long lastOperationTime = 0;

    // ========== CONSTRUCTOR ==========
    public GradeManager() {
        // Initialize our collections
        this.allGrades = new ConcurrentHashMap<>();
        this.gradesByStudent = new ConcurrentHashMap<>();
        this.gradesBySubject = new ConcurrentHashMap<>();

        System.out.println("GradeManager initialized with optimized collections.");
    }

    // ========== BASIC OPERATIONS ==========

    /**
     * Add a new grade to the system.
     * This adds the grade to all our collections for fast access.
     */
    public void addGrade(Grade grade) throws Exception {
        long startTime = System.nanoTime(); // Start timing

        String gradeId = grade.getGradeId();
        String studentId = grade.getStudentId();
        String subjectCode = grade.getSubject().getSubjectCode();

        // Check if grade already exists
        if (allGrades.containsKey(gradeId)) {
            throw new Exception("Grade ID '" + gradeId + "' already exists.");
        }

        // Validate grade value (0-100)
        double gradeValue = grade.getGradeValue();
        if (gradeValue < 0 || gradeValue > 100) {
            throw new Exception("Grade must be between 0 and 100. Got: " + gradeValue);
        }

        // 1. Add to main collection
        allGrades.put(gradeId, grade);

        // 2. Add to student index
        // If student doesn't have grades yet, create a new list for them
        gradesByStudent.computeIfAbsent(studentId, k -> new ArrayList<>())
                .add(grade);

        // 3. Add to subject index
        gradesBySubject.computeIfAbsent(subjectCode, k -> new ArrayList<>())
                .add(grade);

        // Clear cache since we added new data
        clearCache();

        // Update performance tracking
        totalGradesAdded++;
        lastOperationTime = System.nanoTime() - startTime;

        System.out.println("✓ Grade added: " + gradeValue +
                " for student " + studentId +
                " in " + subjectCode);
    }

    /**
     * Get all grades for a specific student.
     * Very fast - uses our student index.
     */
    public List<Grade> getGradesForStudent(String studentId) throws Exception {
        List<Grade> grades = gradesByStudent.get(studentId);

        if (grades == null || grades.isEmpty()) {
            throw new Exception("No grades found for student: " + studentId);
        }

        return new ArrayList<>(grades); // Return copy to protect original
    }

    /**
     * Get all grades for a specific subject.
     */
    public List<Grade> getGradesForSubject(String subjectCode) throws Exception {
        List<Grade> grades = gradesBySubject.get(subjectCode);

        if (grades == null || grades.isEmpty()) {
            throw new Exception("No grades found for subject: " + subjectCode);
        }

        return new ArrayList<>(grades);
    }

    // ========== CALCULATION METHODS (WITH STREAMS) ==========

    /**
     * Calculate average grade for a student.
     * Uses Java Streams for clean, readable code.
     */
    public double calculateStudentAverage(String studentId) throws Exception {
        totalCalculations++;

        // Check cache first
        String cacheKey = "avg:" + studentId;
        SimpleCache cached = calculationCache.get(cacheKey);
        if (cached != null && cached.isFresh()) {
            System.out.println("✓ Using cached average for student: " + studentId);
            return cached.value;
        }

        // Get student's grades
        List<Grade> studentGrades = getGradesForStudent(studentId);

        // Calculate average using Stream API
        double average = studentGrades.stream()
                .mapToDouble(Grade::getGradeValue)  // Convert each grade to its value
                .average()                          // Calculate average
                .orElse(0.0);                       // If no grades, return 0.0

        // Store in cache
        calculationCache.put(cacheKey, new SimpleCache(cacheKey, average));

        System.out.printf("Average for student %s: %.2f%%\n", studentId, average);
        return average;
    }

    /**
     * Calculate average for core subjects only.
     * Shows how to filter with Streams.
     */
    public double calculateCoreAverage(String studentId) throws Exception {
        List<Grade> studentGrades = getGradesForStudent(studentId);

        // Filter for core subjects, then calculate average
        double average = studentGrades.stream()
                .filter(grade -> grade.getSubject().getSubjectType().equals("Core"))
                .mapToDouble(Grade::getGradeValue)
                .average()
                .orElse(0.0);

        System.out.printf("Core average for %s: %.2f%%\n", studentId, average);
        return average;
    }

    /**
     * Calculate average for elective subjects only.
     */
    public double calculateElectiveAverage(String studentId) throws Exception {
        List<Grade> studentGrades = getGradesForStudent(studentId);

        double average = studentGrades.stream()
                .filter(grade -> grade.getSubject().getSubjectType().equals("Elective"))
                .mapToDouble(Grade::getGradeValue)
                .average()
                .orElse(0.0);

        System.out.printf("Elective average for %s: %.2f%%\n", studentId, average);
        return average;
    }

    /**
     * Convert percentage grade to GPA (4.0 scale).
     * 90-100 = 4.0, 80-89 = 3.0, 70-79 = 2.0, 60-69 = 1.0, below 60 = 0.0
     */
    public double calculateGPA(double percentage) {
        if (percentage >= 90) return 4.0;
        else if (percentage >= 80) return 3.0;
        else if (percentage >= 70) return 2.0;
        else if (percentage >= 60) return 1.0;
        else return 0.0;
    }

    /**
     * Calculate GPA for a student.
     */
    public double calculateStudentGPA(String studentId) throws Exception {
        double average = calculateStudentAverage(studentId);
        double gpa = calculateGPA(average);

        System.out.printf("GPA for %s: %.2f (from %.2f%% average)\n",
                studentId, gpa, average);
        return gpa;
    }

    // ========== STATISTICS METHODS ==========

    /**
     * Calculate class-wide statistics.
     * Shows advanced Stream operations.
     */
    public void calculateClassStatistics() {
        System.out.println("\n=== CLASS STATISTICS ===");

        if (allGrades.isEmpty()) {
            System.out.println("No grades available for statistics.");
            return;
        }

        // Get all grade values as a list
        List<Double> allGradeValues = allGrades.values().stream()
                .map(Grade::getGradeValue)
                .collect(Collectors.toList());

        // 1. Basic statistics
        double highest = Collections.max(allGradeValues);
        double lowest = Collections.min(allGradeValues);
        double sum = allGradeValues.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / allGradeValues.size();

        // 2. Median (middle value)
        List<Double> sortedGrades = new ArrayList<>(allGradeValues);
        Collections.sort(sortedGrades);
        double median;
        int middle = sortedGrades.size() / 2;

        if (sortedGrades.size() % 2 == 0) {
            // Even number: average of two middle values
            median = (sortedGrades.get(middle - 1) + sortedGrades.get(middle)) / 2.0;
        } else {
            // Odd number: middle value
            median = sortedGrades.get(middle);
        }

        // 3. Display results
        System.out.println("Total Grades: " + allGradeValues.size());
        System.out.printf("Highest Grade: %.2f%%\n", highest);
        System.out.printf("Lowest Grade: %.2f%%\n", lowest);
        System.out.printf("Average Grade: %.2f%%\n", average);
        System.out.printf("Median Grade: %.2f%%\n", median);
        System.out.printf("Grade Range: %.2f%%\n", highest - lowest);

        // 4. Grade distribution (how many A's, B's, etc.)
        System.out.println("\nGrade Distribution:");
        long aGrades = allGradeValues.stream().filter(g -> g >= 90).count();
        long bGrades = allGradeValues.stream().filter(g -> g >= 80 && g < 90).count();
        long cGrades = allGradeValues.stream().filter(g -> g >= 70 && g < 80).count();
        long dGrades = allGradeValues.stream().filter(g -> g >= 60 && g < 70).count();
        long fGrades = allGradeValues.stream().filter(g -> g < 60).count();

        System.out.println("A (90-100%): " + aGrades + " grades");
        System.out.println("B (80-89%): " + bGrades + " grades");
        System.out.println("C (70-79%): " + cGrades + " grades");
        System.out.println("D (60-69%): " + dGrades + " grades");
        System.out.println("F (Below 60%): " + fGrades + " grades");
    }

    // ========== BATCH OPERATIONS ==========

    /**
     * Add multiple grades at once.
     * Shows error handling for batch operations.
     */
    public Map<String, String> addMultipleGrades(List<Grade> gradesToAdd) {
        Map<String, String> results = new HashMap<>();
        int successCount = 0;

        System.out.println("Adding " + gradesToAdd.size() + " grades...");

        for (Grade grade : gradesToAdd) {
            try {
                addGrade(grade);
                results.put(grade.getGradeId(), "SUCCESS");
                successCount++;
            } catch (Exception e) {
                results.put(grade.getGradeId(), "FAILED: " + e.getMessage());
            }
        }

        System.out.println("Batch add complete: " + successCount + " succeeded, " +
                (gradesToAdd.size() - successCount) + " failed");

        return results;
    }

    // ========== DISPLAY METHODS ==========

    /**
     * Show all grades for a student in a nice format.
     */
    public void displayStudentGrades(String studentId) throws Exception {
        List<Grade> studentGrades = getGradesForStudent(studentId);

        System.out.println("\n=== GRADES FOR STUDENT: " + studentId + " ===");
        System.out.println("Total Grades: " + studentGrades.size());
        System.out.println();

        // Group by subject for better display
        Map<String, List<Grade>> bySubject = studentGrades.stream()
                .collect(Collectors.groupingBy(g -> g.getSubject().getSubjectName()));

        for (Map.Entry<String, List<Grade>> entry : bySubject.entrySet()) {
            System.out.println("Subject: " + entry.getKey());
            for (Grade grade : entry.getValue()) {
                System.out.println("  - " + grade.getGradeValue() + "% (" +
                        grade.getLetterGrade() + ") - " + grade.getDate());
            }
            System.out.println();
        }

        // Show averages
        double overallAvg = calculateStudentAverage(studentId);
        double coreAvg = calculateCoreAverage(studentId);
        double electiveAvg = calculateElectiveAverage(studentId);
        double gpa = calculateStudentGPA(studentId);

        System.out.println("=== SUMMARY ===");
        System.out.printf("Overall Average: %.2f%%\n", overallAvg);
        System.out.printf("Core Subjects Average: %.2f%%\n", coreAvg);
        System.out.printf("Elective Subjects Average: %.2f%%\n", electiveAvg);
        System.out.printf("GPA (4.0 scale): %.2f\n", gpa);
    }

    // ========== HELPER METHODS ==========

    /**
     * Clear the calculation cache.
     */
    private void clearCache() {
        calculationCache.clear();
        System.out.println("Calculation cache cleared");
    }

    /**
     * Get basic statistics about our grades.
     */
    public void showStatistics() {
        System.out.println("\n=== GRADE SYSTEM STATISTICS ===");
        System.out.println("Total Grades: " + allGrades.size());
        System.out.println("Total Students with Grades: " + gradesByStudent.size());
        System.out.println("Total Subjects with Grades: " + gradesBySubject.size());
        System.out.println("Total Grades Added: " + totalGradesAdded);
        System.out.println("Total Calculations: " + totalCalculations);

        if (lastOperationTime > 0) {
            System.out.printf("Last operation took: %.2f ms\n", lastOperationTime / 1_000_000.0);
        }

        // Show cache statistics
        System.out.println("Cache Entries: " + calculationCache.size());

        // Count fresh vs stale cache entries
        long freshCount = calculationCache.values().stream()
                .filter(SimpleCache::isFresh)
                .count();
        System.out.println("Fresh Cache Entries: " + freshCount);
        System.out.println("Stale Cache Entries: " + (calculationCache.size() - freshCount));
    }

    /**
     * Get total number of grades.
     */
    public int getGradeCount() {
        return allGrades.size();
    }

    /**
     * Get number of grades for a specific student.
     */
    public int getGradeCountForStudent(String studentId) {
        List<Grade> grades = gradesByStudent.get(studentId);
        return (grades != null) ? grades.size() : 0;
    }

    /**
     * Clean up expired cache entries.
     */
    public void cleanupCache() {
        int beforeSize = calculationCache.size();
        calculationCache.entrySet().removeIf(entry -> !entry.getValue().isFresh());
        int removed = beforeSize - calculationCache.size();

        if (removed > 0) {
            System.out.println("Cleaned up " + removed + " expired cache entries");
        }
    }

    /**
     * Find students with grades in a specific range.
     * Example: find students with average between 80 and 90.
     */
    public List<String> findStudentsInRange(double min, double max) {
        List<String> studentsInRange = new ArrayList<>();

        for (String studentId : gradesByStudent.keySet()) {
            try {
                double average = calculateStudentAverage(studentId);
                if (average >= min && average <= max) {
                    studentsInRange.add(studentId);
                }
            } catch (Exception e) {
                // Skip students with no grades or errors
            }
        }

        System.out.println("Found " + studentsInRange.size() +
                " students with average between " + min + " and " + max);
        return studentsInRange;
    }
}