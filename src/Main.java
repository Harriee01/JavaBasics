import java.util.*;
import java.util.regex.Pattern;

/**
 * Main console application for Student Grade Management System.
 * Implements Fold 3 requirements with beginner-friendly interface.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static StudentManager studentManager = new StudentManager();
    private static GradeManager gradeManager = new GradeManager();

    // Sample subjects for the system
    private static List<Subject> availableSubjects = Arrays.asList(
            new CoreSubject("Mathematics", "MATH101"),
            new CoreSubject("English", "ENG101"),
            new CoreSubject("Science", "SCI101"),
            new ElectiveSubject("Music", "MUS101"),
            new ElectiveSubject("Art", "ART101"),
            new ElectiveSubject("Physical Education", "PE101")
    );

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   STUDENT GRADE MANAGEMENT SYSTEM");
        System.out.println("            Fold 3 Edition");
        System.out.println("========================================");

        // Create some sample data for testing
        createSampleData();

        // Main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": addStudentMenu(); break;
                    case "2": viewStudentsMenu(); break;
                    case "3": recordGradeMenu(); break;
                    case "4": viewGradeReportMenu(); break;
                    case "5": searchStudentsMenu(); break;
                    case "6": calculateStatisticsMenu(); break;
                    case "7": batchOperationsMenu(); break;
                    case "8": viewSystemStats(); break;
                    case "9": fileOperationsMenu(); break;
                    case "10": running = false; break;
                    default: System.out.println("Invalid choice. Please enter 1-10.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        System.out.println("Thank you for using the system. Goodbye!");
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Record Grade");
        System.out.println("4. View Grade Report");
        System.out.println("5. Search Students");
        System.out.println("6. Calculate Statistics");
        System.out.println("7. Batch Operations");
        System.out.println("8. System Statistics");
        System.out.println("9. File Operations");
        System.out.println("10. Exit");
        System.out.println("==============================");
        System.out.print("Enter your choice (1-10): ");
    }

    private static void addStudentMenu() throws Exception {
        System.out.println("\n=== ADD NEW STUDENT ===");

        // Get student type
        System.out.println("Select student type:");
        System.out.println("1. Regular Student");
        System.out.println("2. Honors Student");
        System.out.print("Enter choice (1 or 2): ");
        String typeChoice = scanner.nextLine();

        if (!typeChoice.equals("1") && !typeChoice.equals("2")) {
            throw new Exception("Please enter 1 for Regular or 2 for Honors.");
        }

        // Get student details with validation
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        InputValidator.validateName(name);

        System.out.print("Enter student email: ");
        String email = scanner.nextLine();
        InputValidator.validateEmail(email);

        System.out.print("Enter student phone: ");
        String phone = scanner.nextLine();
        InputValidator.validatePhone(phone);

        System.out.print("Enter student age: ");
        int age = Integer.parseInt(scanner.nextLine());
        InputValidator.validateAge(age);

        // Create student
        Student student;
        if (typeChoice.equals("1")) {
            student = new RegularStudent(name, email, phone, age);
        } else {
            student = new HonorsStudent(name, email, phone, age);
        }

        // Add to system
        studentManager.addStudent(student);
        System.out.println("✓ Student added successfully!");
    }

    private static void viewStudentsMenu() {
        System.out.println("\n=== VIEW STUDENTS ===");
        System.out.println("Sort by: 1. ID  2. Name  3. Type  4. Age");
        System.out.print("Enter sort choice (1-4): ");
        String sortChoice = scanner.nextLine();

        System.out.print("Enter page number: ");
        int page = Integer.parseInt(scanner.nextLine());

        System.out.print("Items per page: ");
        int pageSize = Integer.parseInt(scanner.nextLine());

        StudentManager.SortOption sortBy;
        switch (sortChoice) {
            case "1": sortBy = StudentManager.SortOption.ID; break;
            case "2": sortBy = StudentManager.SortOption.NAME; break;
            case "3": sortBy = StudentManager.SortOption.TYPE; break;
            case "4": sortBy = StudentManager.SortOption.AGE; break;
            default: sortBy = StudentManager.SortOption.ID;
        }

        studentManager.viewAllStudents(String.valueOf(sortBy), page, pageSize);
    }

    private static void recordGradeMenu() throws Exception {
        System.out.println("\n=== RECORD GRADE ===");

        // Get student ID
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Student student = studentManager.findStudent(studentId);

        // Show available subjects
        System.out.println("\nAvailable Subjects:");
        for (int i = 0; i < availableSubjects.size(); i++) {
            Subject subject = availableSubjects.get(i);
            System.out.println((i + 1) + ". " + subject.getSubjectName() +
                    " (" + subject.getSubjectCode() + ") - " +
                    subject.getSubjectType());
        }

        System.out.print("Select subject (1-" + availableSubjects.size() + "): ");
        int subjectIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (subjectIndex < 0 || subjectIndex >= availableSubjects.size()) {
            throw new Exception("Invalid subject selection.");
        }

        Subject selectedSubject = availableSubjects.get(subjectIndex);

        // Get grade
        System.out.print("Enter grade (0-100): ");
        double gradeValue = Double.parseDouble(scanner.nextLine());
        InputValidator.validateGrade(gradeValue);

        // Create and add grade
        Grade grade = new Grade(studentId, selectedSubject, gradeValue);
        gradeManager.addGrade(grade);

        System.out.println("✓ Grade recorded successfully!");

        // Check honors eligibility
        if (student instanceof HonorsStudent) {
            HonorsStudent honorsStudent = (HonorsStudent) student;
            double average = gradeManager.calculateStudentAverage(studentId);
            honorsStudent.checkHonorsEligibility(average);

            if (honorsStudent.isHonorsEligible()) {
                System.out.println("🎖️ This honors student is now eligible for honors!");
            }
        }
    }

    private static void viewGradeReportMenu() throws Exception {
        System.out.println("\n=== GRADE REPORT ===");
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        gradeManager.displayStudentGrades(studentId);
    }

    private static void searchStudentsMenu() throws Exception {
        System.out.println("\n=== SEARCH STUDENTS ===");
        System.out.println("Search by: 1. Name  2. Type  3. Regex Pattern  4. Grade Range");
        System.out.print("Enter choice (1-4): ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter name to search: ");
                String name = scanner.nextLine();
                List<Student> nameResults = studentManager.searchByName(name);
                displaySearchResults(nameResults);
                break;

            case "2":
                System.out.print("Enter type (Regular/Honors): ");
                String type = scanner.nextLine();
                List<Student> typeResults = studentManager.searchByType(type);
                displaySearchResults(typeResults);
                break;

            case "3":
                System.out.print("Enter regex pattern: ");
                String pattern = scanner.nextLine();
                System.out.print("Search in (name/email/id): ");
                String field = scanner.nextLine();

                StudentManager.SearchField searchField;
                switch (field.toLowerCase()) {
                    case "name": searchField = StudentManager.SearchField.NAME; break;
                    case "email": searchField = StudentManager.SearchField.EMAIL; break;
                    case "id": searchField = StudentManager.SearchField.ID; break;
                    default: throw new Exception("Invalid field");
                }

                List<Student> regexResults = studentManager.searchStudentsWithRegex(pattern, searchField);
                displaySearchResults(regexResults);
                break;

            case "4":
                System.out.print("Enter minimum grade: ");
                double min = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter maximum grade: ");
                double max = Double.parseDouble(scanner.nextLine());
                List<String> rangeResults = gradeManager.findStudentsInRange(min, max);

                System.out.println("\nStudents with average between " + min + " and " + max + ":");
                for (String studentId : rangeResults) {
                    Student student = studentManager.findStudent(studentId);
                    System.out.println("- " + student.getStudentName() + " (" + studentId + ")");
                }
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void displaySearchResults(List<Student> results) {
        if (results.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\nFound " + results.size() + " student(s):");
        for (Student student : results) {
            System.out.println("- " + student.getStudentName() +
                    " (ID: " + student.getStudentId() +
                    ", Type: " + student.getStudentType() +
                    ", Age: " + student.getStudentAge() + ")");
        }
    }

    private static void calculateStatisticsMenu() {
        System.out.println("\n=== STATISTICS ===");
        System.out.println("1. Student Statistics");
        System.out.println("2. Grade Statistics");
        System.out.print("Enter choice (1-2): ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            studentManager.showStatistics();
        } else if (choice.equals("2")) {
            gradeManager.calculateClassStatistics();
            gradeManager.showStatistics();
        }
    }

    private static void batchOperationsMenu() throws Exception {
        System.out.println("\n=== BATCH OPERATIONS ===");
        System.out.println("1. Add Multiple Students");
        System.out.println("2. Add Multiple Grades");
        System.out.print("Enter choice (1-2): ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("How many students to add? ");
            int count = Integer.parseInt(scanner.nextLine());
            List<Student> students = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                System.out.println("\nStudent " + (i + 1) + ":");
                students.add(createStudentFromInput());
            }

            Map<String, String> results = studentManager.addMultipleStudents(students);
            System.out.println("\nBatch operation completed.");

        } else if (choice.equals("2")) {
            System.out.print("How many grades to add? ");
            int count = Integer.parseInt(scanner.nextLine());
            List<Grade> grades = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                System.out.println("\nGrade " + (i + 1) + ":");
                grades.add(createGradeFromInput());
            }

            Map<String, String> results = gradeManager.addMultipleGrades(grades);
            System.out.println("\nBatch operation completed.");
        }
    }

    private static void viewSystemStats() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        studentManager.showStatistics();
        System.out.println();
        gradeManager.showStatistics();
    }

    private static void fileOperationsMenu() {
        System.out.println("\n=== FILE OPERATIONS ===");
        System.out.println("1. Export Student Data");
        System.out.println("2. Export Grade Data");
        System.out.println("3. Cleanup Cache");
        System.out.print("Enter choice (1-3): ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.println("Export feature coming soon!");
        } else if (choice.equals("2")) {
            System.out.println("Export feature coming soon!");
        } else if (choice.equals("3")) {
            studentManager.cleanupCache();
            gradeManager.cleanupCache();
            System.out.println("✓ Cache cleaned up!");
        }
    }

    private static Student createStudentFromInput() throws Exception {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Type (Regular/Honors): ");
        String type = scanner.nextLine();

        if (type.equalsIgnoreCase("Regular")) {
            return new RegularStudent(name, email, phone, age);
        } else {
            return new HonorsStudent(name, email, phone, age);
        }
    }

    private static Grade createGradeFromInput() throws Exception {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Subject Code (e.g., MATH101): ");
        String subjectCode = scanner.nextLine();
        System.out.print("Grade: ");
        double gradeValue = Double.parseDouble(scanner.nextLine());

        // Find subject
        Subject subject = null;
        for (Subject s : availableSubjects) {
            if (s.getSubjectCode().equals(subjectCode)) {
                subject = s;
                break;
            }
        }

        if (subject == null) {
            throw new Exception("Subject not found: " + subjectCode);
        }

        return new Grade(studentId, subject, gradeValue);
    }

    private static void createSampleData() {
        try {
            System.out.println("Creating sample data...");

            // Create sample students
            Student s1 = new RegularStudent("John Doe", "john@school.edu", "555-0101", 18);
            Student s2 = new HonorsStudent("Jane Smith", "jane@school.edu", "555-0102", 19);
            Student s3 = new RegularStudent("Bob Johnson", "bob@school.edu", "555-0103", 20);

            studentManager.addStudent(s1);
            studentManager.addStudent(s2);
            studentManager.addStudent(s3);

            // Create sample grades
            gradeManager.addGrade(new Grade(s1.getStudentId(), availableSubjects.get(0), 85.5));
            gradeManager.addGrade(new Grade(s1.getStudentId(), availableSubjects.get(1), 78.0));
            gradeManager.addGrade(new Grade(s2.getStudentId(), availableSubjects.get(0), 92.5));
            gradeManager.addGrade(new Grade(s2.getStudentId(), availableSubjects.get(3), 88.0));
            gradeManager.addGrade(new Grade(s3.getStudentId(), availableSubjects.get(2), 65.5));

            System.out.println("✓ Sample data created: 3 students, 5 grades");

        } catch (Exception e) {
            System.out.println("Note: Could not create sample data - " + e.getMessage());
        }
    }
}