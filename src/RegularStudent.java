public class RegularStudent extends Student {//a subclass of the Student class which represents a regular student with standard (50) grading requirements
    private final double passingGrade; // this is the passing grade for regular students(50%)

    //This constructor calls the parent constructor(from the Student class) using the keyword "super"
    public RegularStudent(String studentName, String studentEmail, String studentPhone, int studentAge) {
        super (studentName, studentEmail, studentPhone, studentAge);// initializing parent class fields
        this.passingGrade = 50.0;// passing grade is set to 50.0
    }

    //overriding abstract methods from the Student class

    @Override
    public void displayStudentDetails() { // displays all the students details
        System.out.println("ID: " + getStudentId());
        System.out.println("Name: " + getStudentName());
        System.out.println("Type: " + getStudentType());
        System.out.println("Age: " + getStudentAge());
        System.out.println("Email: " + getStudentEmail());
        System.out.println("Average Grade: " + calculateAverageGrade());
        System.out.println("Passing Grade: " + getPassingGrade());
        System.out.println("Status: " + getStudentStatus());
    }

    @Override
    public String getStudentType(){// to get the type of student
        return "Regular";
    }
    @Override
    public double getPassingGrade() { //returns 50.0 as the passing grade for a regular student
        return passingGrade;
    }

    //calculating the average grade from the GradeManager
    @Override
    public double calculateAverageGrade(){
        if (gradeManager == null){//checking if gradeManager reference exists
            return 0.0;// no grade manager available
        }
        return gradeManager.calculateOverallAverage(getStudentId()); // using the GradeManager to calculate overall average
    }

    @Override
    public boolean isPassing(){//checking if student is passing based on passing grade
        double average = calculateAverageGrade();
        return average >= passingGrade; //true if average >=50%
    }

    // implementing GPA calculation - this converts percentage to 4.0 scale
    @Override
    public double calculateGPA() {
        double average = calculateAverageGrade();
        return GPACalculator.percentageToGPA(average);
    }

    // implementing exportToText from Exportable Interface - this creates a text representation for file export
    @Override
    public String exportToText() {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Student ID: ").append(getStudentId()).append("\n");
        stringBuilder1.append("Name: ").append(getStudentName()).append("\n");
        stringBuilder1.append("Type: ").append(getStudentType()).append(" Student\n");
        stringBuilder1.append("Age: ").append(getStudentAge()).append("\n");
        stringBuilder1.append("Email: ").append(getStudentEmail()).append("\n");
        stringBuilder1.append("Phone: ").append(getStudentPhone()).append("\n");
        stringBuilder1.append("Status: ").append(getStudentStatus()).append("\n");
        return stringBuilder1.toString();
    }


}
