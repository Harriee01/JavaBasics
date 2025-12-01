public class StudentManager {

    private Student[] students = new Student[50];  // Array to store students
    private int studentCount = 0;                  // Tracks number of registered students

    // Add a student to the array
    public void addStudent(Student student) {
        if (studentCount < students.length) {
            students[studentCount] = student;
            studentCount++;
        } else {
            System.out.println("Student storage is full.");
        }
    }

    // Find a student by ID
    public Student findStudent(String studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getStudentId().equals(studentId)) {
                return students[i];
            }
        }
        return null;
    }

    // Display all students
    public void viewAllStudents() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }

        System.out.println("\n=== Student List ===");

        for (int i = 0; i < studentCount; i++) {
            Student student = students[i];
            System.out.println("----------------------------------");
            System.out.println("ID: " + student.getStudentId());
            System.out.println("Name: " + student.getStudentName());
            System.out.println("Type: " + student.getStudentType());
            System.out.println("Average Grade: " + student.calculateAverageGrade());
            System.out.println("Passing Grade: " + student.getPassingGrade());
            System.out.println("Status: " + (student.isPassing() ? "Passing" : "Failing"));
        }

        System.out.println("----------------------------------");
        System.out.println("Total Students: " + getStudentCount());
        System.out.println("Class Average: " + getAverageClassGrade());
    }

    // Calculates the class average
    public double getAverageClassGrade() {
        if (studentCount == 0) return 0;

        double sum = 0;
        int counted = 0;

        for (int i = 0; i < studentCount; i++) {
            double avg = students[i].calculateAverageGrade();
            if (avg > 0) {
                sum += avg;
                counted++;
            }
        }

        return counted == 0 ? 0 : (sum / counted);
    }

    // Returns number of students
    public int getStudentCount() {
        return studentCount;
    }
}

