import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * StudentManager that manages all students in the system.
 * Uses optimized collections for better performance and thread safety.
 */
public class StudentManager {

    // main collections, the different ways to organize the student data
    // 1. Primary storage - Fast lookup by student ID
    // ConcurrentHashMap is thread-safe and fast
    private final Map<String, Student> studentsById;

    // 2. Sorted by ID - For when we need students in ID order
    // TreeMap keeps things sorted automatically
    private final Map<String, Student> studentsSortedById;

    // 3. Index by email - To quickly check if email is already used
    private final Set<String> registeredEmails;


    // Cache stores recent search results to avoid re-searching
    private static class SimpleCache {
        String key;                // What we searched for
        List<Student> results;     // The search results
        long timestamp;            // When we cached it

        SimpleCache(String key, List<Student> results) {
            this.key = key;
            this.results = results;
            this.timestamp = System.currentTimeMillis();
        }

        // Check if cache is still fresh (less than 30 seconds old)
        boolean isFresh() {
            return System.currentTimeMillis() - timestamp < 30000;
        }
    }

    // Store our cache entries
    private final Map<String, SimpleCache> searchCache = new HashMap<>();

    // performance tracking
    private long lastOperationTime = 0;
    private int totalStudentsAdded = 0;
    private int totalSearches = 0;

    // constructor for initializing the StudentManager class
    public StudentManager() {
        // Initialize our collections
        this.studentsById = new ConcurrentHashMap<>();
        this.studentsSortedById = new TreeMap<>();
        this.registeredEmails = ConcurrentHashMap.newKeySet();

        System.out.println("StudentManager initialized with optimized collections.");
    }

    //basic operations
    //Add a new student to the system.

    public void addStudent(Student student) throws Exception {
        long startTime = System.nanoTime(); // Start timing

        String studentId = student.getStudentId();
        String email = student.getStudentEmail().toLowerCase();

        // Check if this email already registered
        //synchronized is used to make sure only one thread can check at a time
        synchronized(registeredEmails) {
            if (registeredEmails.contains(email)) {
                throw new Exception("Email '" + email + "' is already registered.");
            }
        }

        // Check if this student ID is already taken
        // putIfAbsent adds only if the ID doesn't exist yet
        Student existing = studentsById.putIfAbsent(studentId, student);
        if (existing != null) {
            throw new Exception("Student ID '" + studentId + "' already exists.");
        }

        // If both checks pass, add to all our collections
        synchronized(studentsSortedById) {
            studentsSortedById.put(studentId, student);
        }

        synchronized(registeredEmails) {
            registeredEmails.add(email);
        }

        // Clear cache since we added a new student
        clearCache();

        // Update performance tracking
        totalStudentsAdded++;
        lastOperationTime = System.nanoTime() - startTime;

        System.out.println("✓ Student added: " + student.getStudentName() +
                " (ID: " + studentId + ")");
    }

    //Find a student by their ID.
    public Student findStudent(String studentId) throws Exception {
        long startTime = System.nanoTime();

        // Direct lookup in our HashMap
        Student student = studentsById.get(studentId);

        if (student == null) {
            throw new Exception("Student with ID '" + studentId + "' not found.");
        }

        lastOperationTime = System.nanoTime() - startTime;
        return student;
    }

    // view students with different sorting options using streams
    public void viewAllStudents(String sortBy, int page, int itemsPerPage) {
        System.out.println("\n=== ALL STUDENTS ===");

        // Get all students as a list
        List<Student> allStudents = new ArrayList<>(studentsById.values());

        if (allStudents.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        // Sort based on user choice
        List<Student> sortedStudents;
        switch (sortBy.toLowerCase()) {
            case "name":
                // Sort by name using Stream API
                sortedStudents = allStudents.stream()
                        .sorted((s1, s2) -> s1.getStudentName().compareToIgnoreCase(s2.getStudentName()))
                        .collect(Collectors.toList());
                break;

            case "id":
                // Already sorted by ID in TreeMap
                sortedStudents = new ArrayList<>(studentsSortedById.values());
                break;

            case "age":
                // Sort by age (youngest first)
                sortedStudents = allStudents.stream()
                        .sorted((s1, s2) -> Integer.compare(s1.getStudentAge(), s2.getStudentAge()))
                        .collect(Collectors.toList());
                break;

            default:
                sortedStudents = allStudents;
        }

        // Calculate pagination
        int totalPages = (int) Math.ceil((double) sortedStudents.size() / itemsPerPage);
        page = Math.max(1, Math.min(page, totalPages)); // Make sure page is valid

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, sortedStudents.size());

        // Display current page
        System.out.println("Total: " + sortedStudents.size() + " students");
        System.out.println("Page " + page + " of " + totalPages + " (Sorted by: " + sortBy + ")");
        System.out.println("Showing students " + (startIndex + 1) + " to " + endIndex);
        System.out.println();

        // Show students on this page
        for (int i = startIndex; i < endIndex; i++) {
            Student student = sortedStudents.get(i);
            System.out.println((i + 1) + ". " + student.getStudentName() +
                    " (ID: " + student.getStudentId() +
                    ", Type: " + student.getStudentType() +
                    ", Age: " + student.getStudentAge() + ")");
        }

        // Show navigation
        if (totalPages > 1) {
            System.out.println("\nNavigation: ");
            if (page > 1) System.out.print("[Previous Page] ");
            if (page < totalPages) System.out.print("[Next Page]");
            System.out.println();
        }
    }

    // search operations
    public List<Student> searchByName(String searchText) {
        totalSearches++;

        // Check cache first
        String cacheKey = "name:" + searchText.toLowerCase();
        if (searchCache.containsKey(cacheKey)) {
            SimpleCache cache = searchCache.get(cacheKey);
            if (cache.isFresh()) {
                System.out.println("✓ Using cached results for: " + searchText);
                return new ArrayList<>(cache.results); // Return copy
            }
        }

        // If not in cache or cache expired, do the search
        List<Student> results = studentsById.values().stream()
                .filter(student ->
                        student.getStudentName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Store in cache
        searchCache.put(cacheKey, new SimpleCache(cacheKey, results));

        System.out.println("Found " + results.size() + " students matching '" + searchText + "'");
        return new ArrayList<>(results); // Return copy
    }

    //Search using regular expressions (advanced pattern matching).

    public List<Student> searchByPattern(String pattern, String field) throws Exception {
        totalSearches++;

        // Check cache
        String cacheKey = "pattern:" + pattern + ":" + field;
        if (searchCache.containsKey(cacheKey)) {
            SimpleCache cache = searchCache.get(cacheKey);
            if (cache.isFresh()) {
                System.out.println("✓ Using cached pattern results");
                return new ArrayList<>(cache.results);
            }
        }

        // Validate pattern
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new Exception("Search pattern cannot be empty");
        }

        try {
            // Create pattern (case-insensitive)
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            List<Student> results = new ArrayList<>();

            // Search based on field
            for (Student student : studentsById.values()) {
                String textToSearch;
                switch (field.toLowerCase()) {
                    case "name":
                        textToSearch = student.getStudentName();
                        break;
                    case "email":
                        textToSearch = student.getStudentEmail();
                        break;
                    case "id":
                        textToSearch = student.getStudentId();
                        break;
                    default:
                        throw new Exception("Invalid search field: " + field);
                }

                if (regex.matcher(textToSearch).find()) {
                    results.add(student);
                }
            }

            // Cache results
            searchCache.put(cacheKey, new SimpleCache(cacheKey, results));

            System.out.println("Pattern search found " + results.size() + " results");
            return results;

        } catch (PatternSyntaxException e) {
            throw new Exception("Invalid search pattern: " + e.getMessage());
        }
    }

    //Search by student type (Regular or Honors).

    public List<Student> searchByType(String type) {
        List<Student> results = studentsById.values().stream()
                .filter(student -> student.getStudentType().equalsIgnoreCase(type))
                .collect(Collectors.toList());

        System.out.println("Found " + results.size() + " " + type + " students");
        return results;
    }

    // batch operations

    /**
     * Add multiple students at once.
     * Shows how to handle multiple operations.
     */
    public Map<String, String> addMultipleStudents(List<Student> studentsToAdd) {
        Map<String, String> results = new HashMap<>();
        int successCount = 0;

        System.out.println("Adding " + studentsToAdd.size() + " students...");

        for (Student student : studentsToAdd) {
            try {
                addStudent(student);
                results.put(student.getStudentId(), "SUCCESS");
                successCount++;
            } catch (Exception e) {
                results.put(student.getStudentId(), "FAILED: " + e.getMessage());
            }
        }

        System.out.println("Batch add complete: " + successCount + " succeeded, " +
                (studentsToAdd.size() - successCount) + " failed");

        return results;
    }

    // helper methods

    /**
     * Clear the search cache.
     */
    private void clearCache() {
        searchCache.clear();
        System.out.println("Search cache cleared");
    }

    /**
     * Get basic statistics about our students.
     */
    public void showStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        System.out.println("Total Students: " + studentsById.size());
        System.out.println("Regular Students: " +
                studentsById.values().stream()
                        .filter(s -> s.getStudentType().equals("Regular"))
                        .count());
        System.out.println("Honors Students: " +
                studentsById.values().stream()
                        .filter(s -> s.getStudentType().equals("Honors"))
                        .count());
        System.out.println("Total Added: " + totalStudentsAdded);
        System.out.println("Total Searches: " + totalSearches);
        System.out.println("Cache Size: " + searchCache.size());

        if (lastOperationTime > 0) {
            System.out.printf("Last operation took: %.2f ms\n", lastOperationTime / 1_000_000.0);
        }
    }

    //Get all students

    public List<Student> getAllStudents() {
        return new ArrayList<>(studentsById.values());
    }

    //Get student count.
    public int getStudentCount() {
        return studentsById.size();
    }

    //This method displays all students

    public void viewAllStudents() {
        viewAllStudents("id", 1, 20); // Default: sorted by ID, first page, 20 items
    }

    /**
     * Remove expired cache entries (basic cleanup).
     */
    public void cleanupCache() {
        int beforeSize = searchCache.size();
        searchCache.entrySet().removeIf(entry -> !entry.getValue().isFresh());
        int removed = beforeSize - searchCache.size();

        if (removed > 0) {
            System.out.println("Cleaned up " + removed + " expired cache entries");
        }
    }
}