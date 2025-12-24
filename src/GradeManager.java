import java.util.ArrayList;
import java.util.List;

// refactored GradeManager following SOLID principles
public class GradeManager {// uses composition; manages all the grades in the system

    private List<Grade> grades; // Changed from array to List for flexibility

    // This constructor initializes an empty grade list.
    public GradeManager() {
        this.grades = new ArrayList<>(); // Using List interface
        AppLogger.info("GradeManager initialized with empty grade list.");
    }

    // this method adds a new grade to the system with validation
    public void addGrade(Grade grade) throws InvalidGradeException, ValidationException {
        AppLogger.enter("addGrade");

        try {
            // Validate grade value
            InputValidator.validateGrade(grade.getGrade());

            // Check for existing grade for same student and subject
            removeExistingGrade(grade.getStudentId(), grade.getSubject());

            // Add grade to list
            grades.add(grade);
            AppLogger.info("Grade added: ID=" + grade.getGradeId() +
                    ", Student=" + grade.getStudentId() +
                    ", Subject=" + grade.getSubject().getSubjectCode() +
                    ", Grade=" + grade.getGrade());

        } finally {
            AppLogger.exit("addGrade");
        }
    }

    //this method removes existing grade for same student and subject to prevent duplicate grades for same subject
    private void removeExistingGrade(String studentId, Subject subject) {
        // Find and remove any existing grade for this student and subject
        grades.removeIf(grade ->
                grade.getStudentId().equals(studentId) &&
                        grade.getSubject().getSubjectCode().equals(subject.getSubjectCode())
        );
    }

    // this method gets all grades for a specific student
    public List<Grade> getGradesByStudent(String studentId) throws StudentNotFoundException {
        AppLogger.enter("getGradesByStudent");

        List<Grade> studentGrades = new ArrayList<>();

        // Filter grades for the specified student
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                studentGrades.add(grade);
            }
        }

        AppLogger.debug("Found " + studentGrades.size() + " grades for student: " + studentId);
        AppLogger.exit("getGradesByStudent");

        return studentGrades;
    }

    // this method displays all grades for a specific student
    public void viewGradesByStudent(String studentId) throws StudentNotFoundException {
        List<Grade> studentGrades = getGradesByStudent(studentId);

        System.out.println("=== Grades for Student: " + studentId + " ===");

        if (studentGrades.isEmpty()) {
            System.out.println("No grades found for this student.");
        } else {
            for (Grade grade : studentGrades) {
                grade.displayGradeDetails();
                System.out.println();
            }
        }

        System.out.println("==================================");
        AppLogger.info("Displayed grades for student: " + studentId);
    }

    // this method calculates the average grade for a student's core subjects only
    public double calculateCoreAverage(String studentId) throws StudentNotFoundException {
        AppLogger.enter("calculateCoreAverage");

        List<Grade> studentGrades = getGradesByStudent(studentId);
        double total = 0.0;
        int count = 0;

        // sums only core subject grades
        for (Grade grade : studentGrades) {
            if (grade.getSubject().getSubjectType().equals("Core")) {
                total += grade.getGrade();
                count++;
            }
        }

        double average = count > 0 ? total / count : 0.0;
        AppLogger.debug("Core average for " + studentId + ": " + average);
        AppLogger.exit("calculateCoreAverage");

        return average;
    }

    // this method calculates the average grade for a student's elective subjects only.
    public double calculateElectiveAverage(String studentId) throws StudentNotFoundException {
        AppLogger.enter("calculateElectiveAverage");

        List<Grade> studentGrades = getGradesByStudent(studentId);
        double total = 0.0;
        int count = 0;

        // Sum only elective subject grades
        for (Grade grade : studentGrades) {
            if (grade.getSubject().getSubjectType().equals("Elective")) {
                total += grade.getGrade();
                count++;
            }
        }

        double average = count > 0 ? total / count : 0.0;
        AppLogger.debug("Elective average for " + studentId + ": " + average);
        AppLogger.exit("calculateElectiveAverage");

        return average;
    }

    // this method calculates overall average grade for a student for all subjects
    public double calculateOverallAverage(String studentId) throws StudentNotFoundException {
        AppLogger.enter("calculateOverallAverage");

        List<Grade> studentGrades = getGradesByStudent(studentId);
        double total = 0.0;
        int count = studentGrades.size();

        // Sum all grades
        for (Grade grade : studentGrades) {
            total += grade.getGrade();
        }

        double average = count > 0 ? total / count : 0.0;
        AppLogger.debug("Overall average for " + studentId + ": " + average);
        AppLogger.exit("calculateOverallAverage");

        return average;
    }

    //this method returns the current number of grades in the system
    public int getGradeCount() {
        return grades.size();
    }



    //this method get the count of grades for a specific student
//    public int getGradeCountForStudent(String studentId) {
//        int count = 0;
//        for (int i = 0; i < gradeCount; i++) {
//            if (grades[i].getStudentId().equals(studentId)) {
//                count++;
//            }
//        }
//        return count;
//    }


    // this method gets all grades (for statistics)
//    public Grade[] getAllGrades() {
//        Grade[] allGrades = new Grade[gradeCount];
//        for (int i = 0; i < gradeCount; i++) {
//            allGrades[i] = grades[i];
//        }
//        return allGrades;
//    }

    //this method gets grades of students by subject name
//    public Grade[] getGradesBySubject(String subjectName) {
//        int count = 0;
//        for (int i = 0; i < gradeCount; i++) {
//            if (grades[i].getSubject().getSubjectName().equalsIgnoreCase(subjectName)) {
//                count++;
//            }
//        }

//        Grade[] subjectGrades = new Grade[count];
//        int index = 0;
//
//        for (int i = 0; i < gradeCount; i++) {
//            if (grades[i].getSubject().getSubjectName().equalsIgnoreCase(subjectName)) {
//                subjectGrades[index++] = grades[i];
//            }
//        }
//
//        return subjectGrades;
//    }

}




























