import java.time.LocalDate;

public class Grade {
    private static int gradeCounter = 1;

    private String gradeId;
    private String studentId;
    private Subject subject;
    private double grade;
    private LocalDate date;

    public Grade(String studentId, Subject subject, double grade) {
        this.studentId = studentId;
        this.subject = subject;
        this.grade = grade;
        this.date = LocalDate.now();

        this.gradeId = String.format("GRD%03d", gradeCounter++);
    }

    public String getGradeId() { return gradeId; }
    public String getStudentId() { return studentId; }
    public Subject getSubject() { return subject; }
    public double getGrade() { return grade; }
    public LocalDate getDate() { return date; }

    public void displayGradeDetails() {
        System.out.println("Grade ID: " + gradeId);
        System.out.println("Date: " + date);
        System.out.println("Student ID: " + studentId);
        System.out.println("Subject: " + subject.getSubjectName());
        System.out.println("Subject Type: " + subject.getSubjectType());
        System.out.println("Grade: " + grade + " (" + getLetterGrade() + ")");
    }

    public String getLetterGrade() {
        if (grade >= 80) return "A";
        if (grade >= 70) return "B";
        if (grade >= 60) return "C";
        if (grade >= 50) return "D";
        return "F";
    }
}