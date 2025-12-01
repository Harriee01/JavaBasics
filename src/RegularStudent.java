public class RegularStudent extends Student {
    private final double passingGrade = 50.0; // this is the passing grade for regular students

    //my Constructor
    public RegularStudent(String studentName, String studentEmail, String studentPhone, int studentAge) {
        super (studentName, studentEmail, studentPhone, studentAge);
    }

    //display student details
    @Override
    public void displayStudentDetails() {
        System.out.println("ID: " + getStudentId());
        System.out.println("Name: " + getStudentName());
        System.out.println("Type: Regular");
        System.out.println("Average Grade: " + calculateAverageGrade());
        System.out.println("Passing Grade: " + getPassingGrade());
        System.out.println("Status: " + (isPassing() ? "Pass" : "Fail"));
    }
    @Override
    public String getStudentType(){
        return "Regular";
    }
    @Override
    public double getPassingGrade() {
        return passingGrade;
    }


}
