import java.util.ArrayList;
import java.util.List;

/**
 * Refactored StudentManager following SOLID principles:
 *  Single Responsibility: Only manages student operations
 *  Open/Closed: Open for extension via interfaces
 *  Liskov Substitution: Works with any Student subclass
 *  Interface Segregation: Uses specific interfaces
 *  Dependency Inversion: Depends on abstractions (List instead of array)
 */

public class StudentManager {// uses composition(it HAS-A array of Students); manages all students in the system
    //private fields specific to StudentManager for managing students
    private List<Student> students;   // Changed from array to List for flexibility (Open/Closed Principle)
//    private int studentCount;    // tracks number of current registered students

    //**referencing the GradeManager class for the sake of calculating averages
    private GradeManager gradeManager;

    //this constructor creates an empty student list
    public StudentManager() {
        this.students = new ArrayList<>();  // using List interface (Dependency inversion)
        AppLogger.info("Student Manager initialized with empty student list");
    }

    //**this method sets the  GradeManager reference
    public void setGradeManager(GradeManager gradeManager01) {
        this.gradeManager = gradeManager01;
    }


    // this method adds a student to the list with validation
    // It throws exceptions instead of returning boolean for cleaner error handling
    public void addStudent(Student student) throws DuplicateStudentException, ValidationException {
        AppLogger.enter("addStudent");

        try {
            // Validate student data before adding
            validateStudentData(student);

            // Check for duplicate student ID
            if (findStudentById(student.getStudentId()) != null) {
                throw new DuplicateStudentException(student.getStudentId());
            }

            // Add student to list
            students.add(student);
            AppLogger.info("Student added: ID=" + student.getStudentId() +
                    ", Name=" + student.getStudentName() +
                    ", Type=" + student.getStudentType());

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

    // finds a student by their ID and throws an exception if a student is not found instead of null
    public Student findStudent(String studentId) throws StudentNotFoundException {
        AppLogger.enter("findStudent");

        try {
            Student student = findStudentById(studentId);
            if (student == null) {
                AppLogger.warning("Student not found: ID=" + studentId);
                throw new StudentNotFoundException(studentId);
            }

            AppLogger.debug("Student found: ID=" + studentId);
            return student;

        } finally {
            AppLogger.exit("findStudent");
        }
    }

    //internal method to find student by ID (returns null if not found)
    //Separated from public method for clarity
    private Student findStudentById(String studentId) {
        // using Java Stream API for cleaner code (functional programming)
        return students.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null); // Return null if not found
    }

    // returns a copy of all students in the system to maintain encapsulation
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Defensive copy
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

    /**
     * Searches for students by name (partial matching, case-insensitive).
     * New feature for Fold 2.
     */
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


    //this method searches students by type (Regular or Honors)
    public Student[] searchByType(String type) {
        int matchCount = 0;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].matchesType(type)) {
                matchCount++;
            }
        }

        Student[] matches = new Student[matchCount];
        int index = 0;

        for (int i = 0; i < studentCount; i++) {
            if (students[i].matchesType(type)) {
                matches[index++] = students[i];
            }
        }

        return matches;
    }

    // this method searches students by grade range
    // for instance minGrade=80, maxGrade=90 finds students with 80-90% average
    public Student[] searchByGradeRange(double minGrade, double maxGrade) {
        int matchCount = 0;
        for (int i = 0; i < studentCount; i++) {
            double avg = students[i].calculateAverageGrade();
            if (avg >= minGrade && avg <= maxGrade) {
                matchCount++;
            }
        }

        Student[] matches = new Student[matchCount];
        int index = 0;

        for (int i = 0; i < studentCount; i++) {
            double avg = students[i].calculateAverageGrade();
            if (avg >= minGrade && avg <= maxGrade) {
                matches[index++] = students[i];
            }
        }

        return matches;
    }
}


