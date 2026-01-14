import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Enhanced StudentManager using optimized Java Collections.
 * Replaces List with HashMap for O(1) lookup and TreeMap for sorted operations.
 * Implements thread-safe operations with ConcurrentHashMap.
 */

public class StudentManager {// uses composition(it HAS-A array of Students); manages all students in the system
    //private fields specific to StudentManager for managing students
    // PRIMARY COLLECTION: ConcurrentHashMap for thread-safe O(1) lookups
    // Key: studentId (String), Value: Student object
    // Using ConcurrentHashMap instead of HashMap for thread safety
    private final Map<String, Student> studentMap;

    // SECONDARY COLLECTION: TreeMap for sorted operations by student ID
    // Automatically maintains students in sorted order by key (studentId)
    private final SortedMap<String, Student> sortedStudentMap;

    // SET for tracking unique email addresses (prevents duplicate emails)
    // Using HashSet for O(1) membership checks
    private final Set<String> emailRegistry;

    // Performance monitoring to track operation times
    private long lastAddStudentTime;
    private long lastFindStudentTime;


    //this constructor initializes all collections, it also uses ConcurrentHashMap for thread safety in the multi-threaded environment.
    public StudentManager() {
        // ConcurrentHashMap: Thread-safe, high-concurrency hash table
        this.studentMap = new ConcurrentHashMap<>();

        // TreeMap: Red-Black tree implementation, maintains sorted order
        // Wrapping with Collections.synchronizedSortedMap for thread safety
        this.sortedStudentMap = Collections.synchronizedSortedMap(new TreeMap<>());

        // HashSet: Fast O(1) add/remove/contains operations
        // Wrapping with Collections.synchronizedSet for thread safety
        this.emailRegistry = Collections.synchronizedSet(new HashSet<>());

        this.lastAddStudentTime = 0;
        this.lastFindStudentTime = 0;

        AppLogger.info("StudentManager initialized with optimized collections.");
    }

    //**this method sets the  GradeManager reference
    //public void setGradeManager(GradeManager gradeManager01) {
        //this.gradeManager = gradeManager01;
    //}


    // this method adds a student with O(1) average time complexity.
    // It throws exceptions instead of returning boolean for cleaner error handling and uses ConcurrentHashMap.putIfAbsent() for atomic thread-safe operation.
    public void addStudent(Student student) throws DuplicateStudentException, ValidationException {
        AppLogger.enter("addStudent");

        try {
            // Validate student data before any operation
            validateStudentData(student);

            String studentId = student.getStudentId();
            String email = student.getStudentEmail();

            // Thread-safe check for duplicate email using synchronized block
            synchronized(emailRegistry) {
                if (emailRegistry.contains(email)) {
                    throw new DuplicateStudentException("Email '" + email + "' already registered.");
                }
            }

            // Thread-safe atomic operation: put if absent
            // Returns null if key was absent, returns existing value if key exists
            Student existing = studentMap.putIfAbsent(studentId, student);

            if (existing != null) {
                // Student ID already exists - rollback email registration
                synchronized(emailRegistry) {
                    emailRegistry.remove(email);
                }
                throw new DuplicateStudentException(studentId);
            }

            // Update secondary collections
            synchronized(sortedStudentMap) {
                sortedStudentMap.put(studentId, student);
            }

            synchronized(emailRegistry) {
                emailRegistry.add(email);
            }

            // Performance logging
            lastAddStudentTime = System.nanoTime() - startTime;
            AppLogger.info(String.format(
                    "Student added in %d ns: ID=%s, Name=%s, Type=%s",
                    lastAddStudentTime, studentId, student.getStudentName(), student.getStudentType()
            ));

        } finally {
            AppLogger.exit("addStudent");
        }
    }


    // This method validates all student data before adding  to system
    // A separate method for Single Responsibility.
    private void validateStudentData(Student student) throws ValidationException {
        // Validate all student fields using InputValidator
        InputValidator.validateName(student.getStudentName());
        InputValidator.validateAge(student.getStudentAge());
        InputValidator.validateEmail(student.getStudentEmail());
        InputValidator.validatePhone(student.getStudentPhone());
        InputValidator.validateStudentId(student.getStudentId());
    }

    // finds a student by their ID with O(1) average time complexity and throws an exception if a student is not found instead of null
    // ConcurrentHashMap.get() is used;  thread-safe for reads.
    public Student findStudent(String studentId) throws StudentNotFoundException {
        long startTime = System.nanoTime(); // Start performance timer
        AppLogger.enter("findStudent");

        try {
            // O(1) lookup in ConcurrentHashMap
            Student student = studentMap.get(studentId);

            if (student == null) {
                AppLogger.warning("Student not found: ID=" + studentId);
                throw new StudentNotFoundException(studentId);
            }

            // Performance logging
            lastFindStudentTime = System.nanoTime() - startTime;
            AppLogger.debug(String.format(
                    "Student found in %d ns: ID=%s", lastFindStudentTime, studentId
            ));

            return student;

        } finally {
            AppLogger.exit("findStudent");
        }
    }

    //internal method to find student by ID (returns null if not found)
    //Separated from public method for clarity
//    private Student findStudentById(String studentId) {
//        // using Java Stream API for cleaner code (functional programming)
//        return students.stream()
//                .filter(student -> student.getStudentId().equals(studentId))
//                .findFirst()
//                .orElse(null); // Return null if not found
//    }

    // returns a copy of all students sorted by ID using TreeMap.
    //  TreeMap maintains natural ordering of keys (student IDs) in the system
    public List<Student> getAllStudentsSortedById() {
        AppLogger.enter("getAllStudentsSortedById");

        // TreeMap.values() returns values in key-sorted order
        // Creating defensive copy to prevent external modification
        List<Student> sortedStudents;
        synchronized(sortedStudentMap) {
            sortedStudents = new ArrayList<>(sortedStudentMap.values());
        }

        AppLogger.debug("Returned " + sortedStudents.size() + " students sorted by ID.");
        AppLogger.exit("getAllStudentsSortedById");

        return sortedStudents;
    }

    /**
     * Returns students sorted by name using Stream API and Comparator.
     * Demonstrates functional programming with Streams.
     */
    public List<Student> getAllStudentsSortedByName() {
        AppLogger.enter("getAllStudentsSortedByName");

        // Using Java Stream API for functional, declarative programming
        List<Student> sortedStudents = studentMap.values().stream()
                .sorted(Comparator.comparing(Student::getStudentName)) // Comparator for name sorting
                .collect(Collectors.toList()); // Terminal operation to collect results

        AppLogger.debug("Returned " + sortedStudents.size() + " students sorted by name.");
        AppLogger.exit("getAllStudentsSortedByName");

        return sortedStudents;
    }

    /**
     * Enhanced search with regex pattern matching.
     * Searches both name and email using regular expressions.
     */
    public List<Student> searchStudentsWithRegex(String regexPattern, SearchField field)
            throws ValidationException {

        AppLogger.enter("searchStudentsWithRegex");

        try {
            // Validate regex pattern for safety
            validateRegexPattern(regexPattern);

            // Compile pattern once for efficiency
            Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
            List<Student> results = new ArrayList<>();

            // Choose search field
            switch (field) {
                case NAME:
                    // Search in student names using regex
                    for (Student student : studentMap.values()) {
                        if (pattern.matcher(student.getStudentName()).find()) {
                            results.add(student);
                        }
                    }
                    break;

                case EMAIL:
                    // Search in student emails using regex
                    // Example: ".*@university\\.edu$" finds all university emails
                    for (Student student : studentMap.values()) {
                        if (pattern.matcher(student.getStudentEmail()).find()) {
                            results.add(student);
                        }
                    }
                    break;

                case ID:
                    // Search in student IDs using regex
                    for (Student student : studentMap.values()) {
                        if (pattern.matcher(student.getStudentId()).find()) {
                            results.add(student);
                        }
                    }
                    break;
            }

            AppLogger.info(String.format(
                    "Regex search '%s' on field %s found %d results",
                    regexPattern, field, results.size()
            ));

            return results;

        } catch (PatternSyntaxException e) {
            throw new ValidationException("Invalid regex pattern: " + e.getMessage());
        } finally {
            AppLogger.exit("searchStudentsWithRegex");
        }
    }

    /**
     * Returns performance metrics for monitoring.
     */
    public Map<String, Long> getPerformanceMetrics() {
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("lastAddStudentTime", lastAddStudentTime);
        metrics.put("lastFindStudentTime", lastFindStudentTime);
        metrics.put("studentCount", (long) studentMap.size());
        metrics.put("emailRegistrySize", (long) emailRegistry.size());
        return metrics;
    }

    /**
     * Enum for search field types.
     * Ensures type safety compared to using raw strings.
     */
    public enum SearchField {
        NAME, EMAIL, ID
    }



    // this method displays all students in the system
    public void viewAllStudents() {

        AppLogger.enter("viewAllStudents");
        System.out.println("=== ALL STUDENTS ===");
        System.out.println("Total Students: " + students.size());
        System.out.println();

        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            AppLogger.info("No students to display.");
            return;
        }

        // using an enhanced for loop for cleaner syntax
        for (Student student : students) {
            student.displayStudentDetails();
            System.out.println();
        }

        AppLogger.info("Displayed " + students.size() + " students.");
        AppLogger.exit("viewAllStudents");
    }

    // this method calculates the average grade for the entire class
    public double getAverageClassGrade() {
        AppLogger.enter("getAverageClassGrade");

        if (students.isEmpty()) {
            AppLogger.debug("No students, returning 0.0");
            return 0.0;
        }

        double totalAverage = 0.0;
        int studentsWithGrades = 0;

        // calculating the average for each student
        for (Student student : students) {
            double studentAverage = student.calculateAverageGrade();

            // only includes students who have grades
            if (studentAverage > 0) {
                totalAverage += studentAverage;
                studentsWithGrades++;
            }
        }

        double classAverage = studentsWithGrades > 0 ? totalAverage / studentsWithGrades : 0.0;
        AppLogger.debug("Class average calculated: " + classAverage);
        AppLogger.exit("getAverageClassGrade");

        return classAverage;
    }

    // returns the number of students in the system
    public int getStudentCount() {
        return students.size();
    }

    // this method searches for students by name (partial matching, case-insensitive)
    public List<Student> searchByName(String partialName) {
        AppLogger.enter("searchByName");

        List<Student> results = new ArrayList<>();
        String searchTerm = partialName.toLowerCase();

        // filters students whose name contains the search term
        for (Student student : students) {
            if (student.getStudentName().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }

        AppLogger.info("Name search for '" + partialName + "' found " + results.size() + " results.");
        AppLogger.exit("searchByName");

        return results;
    }

    // this method searches for students by type (Regular or Honors)
    public List<Student> searchByType(String type) {
        AppLogger.enter("searchByType");

        List<Student> results = new ArrayList<>();
        String searchTerm = type.toLowerCase();

        for (Student student : students) {
            if (student.getStudentType().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }

        AppLogger.info("Type search for ' " + type + " ' found" + results.size() + " results. ");
        AppLogger.exit("searchByType");

        return results;

    }

    // this method searches students by grade range
    // for instance minGrade=80, maxGrade=90 finds students with 80-90% average
    public List<Student> searchByGradeRange(double minGrade, double maxGrade) {
        AppLogger.enter("searchByGradeRange");

        List<Student> results = new ArrayList<>();
        // Validate input range
        if (minGrade < 0 || maxGrade > 100 || minGrade > maxGrade) {
            AppLogger.warning("Invalid grade range: " + minGrade + " - " + maxGrade);
            return results; // return empty list
        }

        // Filter students by average grade
        for (Student student : students) {
            double average = student.calculateAverageGrade();

            // Only include students who actually have grades
            if (average > 0 && average >= minGrade && average <= maxGrade) {
                results.add(student);
            }
        }

        AppLogger.info(
                "Grade range search (" + minGrade + "–" + maxGrade +
                        ") found " + results.size() + " students."
        );

        AppLogger.exit("searchStudentsByGradeRange");
        return results;

    }


    // this method displays a summary report of all students
    public void displayStudentSummary() {
        System.out.println("=== STUDENT SUMMARY REPORT ===");
        System.out.println("Total Students: " + students.size());

        int regularCount = 0;
        int honorsCount = 0;
        int passingCount = 0;

        // Count different types of students
        for (Student student : students) {
            if (student.getStudentType().equals("Regular")) {
                regularCount++;
            } else {
                honorsCount++;
            }

            if (student.isPassing()) {
                passingCount++;
            }
        }

        System.out.println("Regular Students: " + regularCount);
        System.out.println("Honors Students: " + honorsCount);
        System.out.println("Passing Students: " + passingCount);
        System.out.printf("Class Average: %.2f%%\n", getAverageClassGrade());
        System.out.println("===============================");

        AppLogger.info("Displayed student summary: " + students.size() +
                " students, " + passingCount + " passing.");
    }

}



