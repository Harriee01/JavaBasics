public class HonorsStudent extends Student {// a subclass of the Student class which represents an honors student with higher (60+) passing standards
    //private fields specific to honors students
    private boolean honorsEligible;//this checks whether a student qualifies for honors
    private final double passingGrade; // this is the minimum passing grade for honors students(60)

    //This constructor calls the parent constructor(from the Student class) using the keyword "super"
    public HonorsStudent(String studentName, String studentEmail, String studentPhone, int studentAge) {
        super (studentName, studentEmail, studentPhone, studentAge);// initializing parent fields
        this.passingGrade = 60.0;
        this.honorsEligible = false; //initially not eligible until proven
    }

    // overriding abstract methods from Student class
    @Override
    public void displayStudentDetails() { // display student details
        //double average = calculateAverageGrade();
        System.out.println("ID: " + getStudentId());
        System.out.println("Name: " + getStudentName());
        System.out.println("Type: " +  getStudentType());
        System.out.println("Age: " + getStudentAge());
        System.out.println("Email: " + getStudentEmail());
        System.out.println("Average Grade: " + calculateAverageGrade());
        System.out.println("Passing Grade: " + getPassingGrade() + "%");
        System.out.println("Honors Eligible: " + (honorsEligible ? "Yes" : "No"));
        System.out.println("Status: " + getStudentStatus());
    }
    @Override
    public String getStudentType(){// to get the type of student
        return "Honors";
    }
    @Override
    public double getPassingGrade() {//returns the passing grade for an honors student
        return passingGrade;
    }
    @Override
    public double calculateAverageGrade() {
        if (gradeManager == null) {// checking if gradeManager reference exists
            return 0.0;  // meaning no grade manager available
        }
        return gradeManager.calculateOverallAverage(getStudentId());// using GradeManager to calculate overall average
    }
    @Override
    public boolean isPassing() {//checking if student is passing based on passing grade
        double average = calculateAverageGrade();
        return average >= passingGrade;  //true if average >= 60%
    }

    //this method checks honors eligibility (average >= 85%)
    public boolean checkHonorsEligibility() {
        double average = calculateAverageGrade();
        honorsEligible = average >= 85.0;  // eligibility status is then updated
        return honorsEligible;
    }

    // Getter for honors eligibility
    public boolean isHonorsEligible() {
        return honorsEligible;
    }

//    public boolean checkHonorsEligibility() {
//        return calculateAverageGrade() >= 85.0;
//    }

}
