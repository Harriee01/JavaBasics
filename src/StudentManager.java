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
    public void addStudent(Student student) throws DuplicateStudentException, ValidationException{
        AppLogger.enter("addStudent");

        try{
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

        }finally {
            AppLogger.exit("addStudent");
        }
//        if (studentCount < students.length) {// checking if the array is not full
//            students[studentCount] = student;// the student is added at the next available position
//            student.setGradeManager(gradeManager);// the gradeManager reference for the student is set
//            studentCount++;// increment count
//        } else {
//            System.out.println("Student storage is full.");
//        }
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
//    // this method finds a student by ID
//    public Student findStudent(String studentId) {
//        for (int i = 0; i < studentCount; i++) {//looping through all the students
//            if (students[i].getStudentId().equals(studentId)) {//checking if the current student's ID is a match
//                return students[i];// found so it returns the student
//            }
//        }
//        return null;// not found so it returns null
//    }

    // returns a copy of all students in the system to maintain encapsulation
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Defensive copy
    }

    // this method searches students by name (partial match)
    // returns array of matching students
    public Student[] searchByName(String name) {
        // first count matches
        int matchCount = 0;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].matchesName(name)) {
                matchCount++;
            }
        }

        // creates array of exact size
        Student[] matches = new Student[matchCount];
        int index = 0;

        // fills array with matches
        for (int i = 0; i < studentCount; i++) {
            if (students[i].matchesName(name)) {
                matches[index++] = students[i];
            }
        }

        return matches;
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


    // this method displays all students
    public void viewAllStudents() {
        if (studentCount == 0) {//checking if there are any students
            System.out.println("No students registered.");
            return;
        }

        System.out.println("\n=== Student List ===");
        //looping through to display each student
        for (int i = 0; i < studentCount; i++) {
            Student student = students[i];
            double avgGrade = student.calculateAverageGrade();//calculates actual average grade for the student
            String statusText = avgGrade ==0 ? "Active": (student.isPassing() ? "Passed" : "Failed");
            System.out.println("----------------------------------");
            System.out.println("ID: " + student.getStudentId());
            System.out.println("Name: " + student.getStudentName());
            System.out.println("Type: " + student.getStudentType());
            System.out.println("Average Grade: " + avgGrade);
            System.out.println("Passing Grade: " + student.getPassingGrade());
            System.out.println("Status: " + statusText);

            // showing additional information for Honors students
            if (student instanceof HonorsStudent) {
                HonorsStudent honorsStudent01 = (HonorsStudent) student;  // Type casting
                // counting enrolled subjects (grades) for the student
                int enrolledSubjects = gradeManager.getGradeCountForStudent(student.getStudentId());
                System.out.println("           | Enrolled Subjects: " + enrolledSubjects +
                        " | Passing Grade: " + student.getPassingGrade() + "% | Honors Eligible");
            } else {
                // counting enrolled subjects for regular student
                int enrolledSubjects = gradeManager.getGradeCountForStudent(student.getStudentId());
                System.out.println("           | Enrolled Subjects: " + enrolledSubjects +
                        " | Passing Grade: " + student.getPassingGrade() + "%");
            }
            System.out.println("----------------------------------");
        }


        System.out.println("\nTotal Students: " + getStudentCount());
        System.out.println("Class Average: " + getAverageClassGrade());
    }

    //this method calculates the average grade of all students
    public double getAverageClassGrade() {
        if (studentCount == 0){ //checking if there are no students
            return 0.0;
        }
        double totalAverage = 0.0;// the sum of all student averages
        int countStudentsWithGrades = 0;//takes the count of students who have grades
        for (int i = 0; i < studentCount; i++) {//looping through all the students and summing their averages
            double studentAvg = students[i].calculateAverageGrade();
            if (studentAvg > 0) {// only count students who have at least one grade
                totalAverage += studentAvg;
                countStudentsWithGrades++;
            }
        }
        if(countStudentsWithGrades == 0){//avoiding division by zero
            return 0.0;
        }

        return totalAverage / countStudentsWithGrades;// return the average of all student averages
    }

    // this method returns the total number of students
    public int getStudentCount() {
        return studentCount;
    }

    //this method gets all students (for statistics)
//    public Student[] getAllStudents() {
//        Student[] allStudents = new Student[studentCount];
//        for (int i = 0; i < studentCount; i++) {
//            allStudents[i] = students[i];
//        }
//        return allStudents;
//    }
}

