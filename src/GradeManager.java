public class GradeManager {

    private Grade[] grades = new Grade[200];  // Array to store grades
    private int gradeCount = 0;               // Tracks how many grades have been added

    // Adds a grade to the array
    public void addGrade(Grade grade) {
        if (gradeCount < grades.length) {
            grades[gradeCount] = grade;
            gradeCount++;
        } else {
            System.out.println("Grade storage is full.");
        }
    }

    // Displays all grades for a specific student
    public void viewGradesByStudent(String studentId) {
        if (gradeCount == 0) {
            System.out.println("No grades recorded yet.");
            return;
        }

        System.out.println("\n=== Grade Report for Student ID: " + studentId + " ===");

        boolean foundAny = false;

        // Print grades in reverse order (newest first)
        for (int i = gradeCount - 1; i >= 0; i--) {
            Grade grade = grades[i];
            if (grade != null && grade.getStudentId().equals(studentId)) {
                foundAny = true;
                System.out.println("--------------------------------------");
                grade.displayGradeDetails();
            }
        }

        if (!foundAny) {
            System.out.println("No grades found for this student.");
            return;
        }

        // Show averages
        double coreAverage = calculateCoreAverage(studentId);
        double electiveAverage = calculateElectiveAverage(studentId);
        double overallAverage = calculateOverallAverage(studentId);

        System.out.println("\n--- Averages ---");
        System.out.println("Core Subjects Average: " + (coreAverage >= 0 ? coreAverage : "N/A"));
        System.out.println("Elective Subjects Average: " + (electiveAverage >= 0 ? electiveAverage : "N/A"));
        System.out.println("Overall Average: " + (overallAverage >= 0 ? overallAverage : "N/A"));

        // Performance summary
        System.out.println("\n--- Performance Summary ---");
        if (overallAverage >= 0) {
            if (overallAverage >= 50) System.out.println("Status: Passing");
            else System.out.println("Status: Failing");
        } else {
            System.out.println("No grades available to determine performance.");
        }

        System.out.println("--------------------------------------\n");
    }

    // Calculates average of CORE subjects
    public double calculateCoreAverage(String studentId) {
        double sum = 0;
        int count = 0;

        for (int i = 0; i < gradeCount; i++) {
            Grade grade = grades[i];
            if (grade != null && grade.getStudentId().equals(studentId)) {
                if (grade.getSubject().getSubjectType().equals("Core")) {
                    sum += grade.getGrade();
                    count++;
                }
            }
        }

        return (count == 0) ? -1 : (sum / count); // -1 means no core grades
    }

    // Calculates average of ELECTIVE subjects
    public double calculateElectiveAverage(String studentId) {
        double sum = 0;
        int count = 0;

        for (int i = 0; i < gradeCount; i++) {
            Grade grade = grades[i];
            if (grade != null && grade.getStudentId().equals(studentId)) {
                if (grade.getSubject().getSubjectType().equals("Elective")) {
                    sum += grade.getGrade();
                    count++;
                }
            }
        }

        return (count == 0) ? -1 : (sum / count); // -1 means no elective grades
    }

    // Calculates overall average (Core + Elective)
    public double calculateOverallAverage(String studentId) {
        double sum = 0;
        int count = 0;

        for (int i = 0; i < gradeCount; i++) {
            Grade grade = grades[i];
            if (grade != null && grade.getStudentId().equals(studentId)) {
                sum += grade.getGrade();
                count++;
            }
        }

        return (count == 0) ? -1 : (sum / count); // -1 means no grades
    }

    // Returns how many grades are stored
    public int getGradeCount() {
        return gradeCount;
    }
}
