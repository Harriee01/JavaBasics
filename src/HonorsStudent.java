public class HonorsStudent extends Student {
    private final double passingGrade = 60.0; // this is the passing grade for honors students
    private boolean honorsEligible;//this checks whether a student qualifies for honors

    //my Constructor
    public HonorsStudent(String studentName, String studentEmail, String studentPhone, int studentAge) {
        super (studentName, studentEmail, studentPhone, studentAge);
    }

    //display student details
    @Override
    public void displayStudentDetails() {
        double average = calculateAverageGrade();
        System.out.println("ID: " + getStudentId());
        System.out.println("Name: " + getStudentName());
        System.out.println("Type: Honors");
        System.out.println("Average Grade: " + average);
        System.out.println("Passing Grade: " + getPassingGrade());
        System.out.println("Honors Eligible: " + (honorsEligible ? "Yes" : "No"));
        System.out.println("Status: " + (isPassing() ? "Pass" : "Fail"));
    }
    @Override
    public String getStudentType(){
        return "Honors";
    }
    @Override
    public double getPassingGrade() {
        return passingGrade;
    }

    public boolean checkHonorsEligibility() {
        return calculateAverageGrade() >= 85.0;
    }

}
