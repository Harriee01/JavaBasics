import java.util.ArrayList;// used to import Java's ArrayList class to store lists of grades

public abstract class Student implements Gradable { // Student class is abstract because it cannot be instantiated and is used as the parent class for all student types
    //all the fields are private so that they can be modified and accessed only within this abstract class
    // it also implements the Gradable interface and provides (a must) for the methods stated in the interface

    private String studentId; //unique student ID
    private String studentName; // Student's full name
    private String studentEmail;// Student's email address
    private String studentPhone;//Student's phone number
    private int studentAge; // Student's age
    private String studentStatus; // Student's current status be it active or inactive
    private static int studentCounter = 1; //  static counter to generate the unique student IDs

    //running totals for average calculation
    // private double totalGrades = 0.0;
    //private int gradeCount = 0;
    // protected ArrayList<Double> grades = new ArrayList<>(); // this list is for storing the grades of a student

    //referencing GradeManager which is needed to calculate averages; it will be set from the main class
    protected GradeManager gradeManager;


    // Constructor to initialize a new Student object
    public Student(String studentName, String studentEmail, String studentPhone, int studentAge) {
        studentCounter++; //increments counter for every new student
        this.studentId = String.format("STU" + studentCounter);
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.studentAge = studentAge;
        this.studentStatus = "ACTIVE"; // the default status for every new student
    }

    //Getter methods to read private fields
    public String getStudentId() {return studentId;}
    public String getStudentName() {return studentName;}
    public String getStudentEmail() {return studentEmail;}
    public String getStudentPhone() {return studentPhone;}
    public int getStudentAge() {return studentAge;}
    public String getStudentStatus() {return studentStatus;}

    //Setter methods to allow other classes modify private fields
    public void setStudentName(String studentName){this.studentName = studentName;}
    public void setStudentEmail(String studentEmail){this.studentEmail = studentEmail;}
    public void setStudentPhone(String studentPhone){this.studentPhone = studentPhone;}
    public void setStudentAge(int studentAge){this.studentAge = studentAge;}
    public void setStudentStatus(String studentStatus){this.studentStatus = studentStatus;}

    //this method sets the GradeManager reference
    public void setGradeManager(GradeManager gradesManager){this.gradeManager = gradesManager;}

    //implementing the Gradable interface method, validateGrade to validate if a grade is between 0 and 100
    @Override
    public boolean validateGrade(double grade) {
        return grade >= 0 && grade <= 100; //returns true if valid, false if not
    }

    //implementing the Gradable interface method, recordGrade to record a grade(used as a placeholder for now)
    @Override
    public boolean recordGrade(double grade){
        //checking if grade is valid before recording
        if (validateGrade(grade)) {
            return true; // grade is valid and can be recorded
        }
        return false;
    }

    //public void addGrade(double grade) { //adds a grade to the student record
//        grades.add(grade);  // stores the grades
//        totalGrades += grade; //updates total
//        gradeCount++; //increases grade count
//    }
//
//    public double calculateAverageGrade() { // this method calculates the average grade
//        if(gradeCount == 0) return 0.0;
//        return totalGrades / gradeCount;
//    }

   // public boolean isPassing(){ // this method checks if a student passes based on passing grade
//        return calculateAverageGrade() >= getPassingGrade();
//    }

    //Abstract methods that must be implemented in subclasses
    public abstract double getPassingGrade();// returns the minimum passing grade
    public abstract String getStudentType();//returns the type of student, whether regular or honors
    public abstract void displayStudentDetails();// shows the student information
    public abstract double calculateAverageGrade();// calculates the average of all the grades
    public abstract boolean isPassing();// checks if the student is passing

}
