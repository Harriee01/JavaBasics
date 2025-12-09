import java.time.LocalDate;
//this class represents a grade record for a student in a subject
public class Grade {
    //this is a static field shared by all Grade objects used to generate unique IDs
    private static int gradeCounter = 1;
    //private fields specific to each Grade object
    private String gradeId; // unique identifier
    private String studentId;// ID of the student who has the grade
    private Subject subject; // the subject the grade is for
    private double grade;// the actual grade from 0-100
    private LocalDate date; // the date the grade was recorded

    //this constructor creates a new Grade object
    public Grade(String studentId, Subject subject, double grade) {
        gradeCounter++;// increment the counter for each new grade
        this.gradeId = String.format("GRD%03d", gradeCounter);

        this.studentId = studentId;
        this.subject = subject;
        this.grade = grade;
        this.date = LocalDate.now();

    }

    //Getter methods for all the private fields in the class
    public String getGradeId() { return gradeId; }
    public String getStudentId() { return studentId; }
    public Subject getSubject() { return subject; }
    public double getGrade() { return grade; }
    public LocalDate getDate() { return date; }

    //this method displays all grade information
    public void displayGradeDetails() {
        System.out.println("Grade ID: " + gradeId);
        System.out.println("Date: " + date);
        System.out.println("Student ID: " + studentId);
        System.out.println("Subject: " + subject.getSubjectName());
        System.out.println("Subject Type: " + subject.getSubjectType());
        System.out.println("Grade: " + grade + " (" + getLetterGrade() + ")");
    }

    //this method converts numerical grade to letter grade
    public String getLetterGrade() {
        //if-else chain is used to determine letter grade
        if (grade >= 80) return "A";
        else if (grade >= 70) return "B";
        else if (grade >= 60) return "C";
        else if (grade >= 50) return "D";
        else return "F";
    }
}