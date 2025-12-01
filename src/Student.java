import java.util.ArrayList;

public abstract class Student {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private int studentAge;
    private String studentStatus;
    //running totals for average calculation
    private double totalGrades = 0.0;
    private int gradeCount = 0;

    protected ArrayList<Double> grades = new ArrayList<>(); // this list is for storing the grades of a student

    private static int studentCounter = 1; // I used this static counter to generate the unique student IDs

    // Constructor
    public Student(String studentName, String studentEmail, String studentPhone, int studentAge) {
        this.studentId = "STU" + studentCounter++; // this is where the generation of the unique ID is implemented
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.studentAge = studentAge;
        this.studentStatus = "ACTIVE"; // the default status
    }

    //Getter methods
    public String getStudentId() {return studentId;}
    public String getStudentName() {return studentName;}
    public String getStudentEmail() {return studentEmail;}
    public String getStudentPhone() {return studentPhone;}
    public int getStudentAge() {return studentAge;}
    public String getStudentStatus() {return studentStatus;}

    //Setter methods
    public void setStudentName(String studentName){this.studentName = studentName;}
    public void setStudentEmail(String studentEmail){this.studentEmail = studentEmail;}
    public void setStudentPhone(String studentPhone){this.studentPhone = studentPhone;}
    public void setStudentAge(int studentAge){this.studentAge = studentAge;}
    public void setStudentStatus(String studentStatus){this.studentStatus = studentStatus;}

    public void addGrade(double grade) { //adds a grade to the student record
        grades.add(grade);  // stores the grades
        totalGrades += grade; //updates total
        gradeCount++; //increases grade count
    }

    public double calculateAverageGrade() { // this method calculates the average grade
        if(gradeCount == 0) return 0.0;
        return totalGrades / gradeCount;
    }

    public boolean isPassing(){ // this method checks if a student passes based on passing grade
        return calculateAverageGrade() >= getPassingGrade();
    }

    //Abstract methods that are implemented in subclasses
    public abstract double getPassingGrade();
    public abstract String getStudentType();
    public abstract void displayStudentDetails();

}
