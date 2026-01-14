public class RegularStudent extends Student {
    private static final String STUDENT_TYPE = "Regular";
    private static final double PASSING_GRADE = 50.0;

    public RegularStudent(String name, String email, String phone, int age) {
        super(name, email, phone, age, STUDENT_TYPE);
    }

    @Override
    public double getPassingGrade() {
        return PASSING_GRADE;
    }

    @Override
    public void displayStudentDetails() {
        System.out.printf("Regular Student: %s (ID: %s)%n",
                getStudentName(), getStudentId());
        System.out.printf("  Email: %s, Phone: %s, Age: %d%n",
                getStudentEmail(), getStudentPhone(), getStudentAge());
        System.out.printf("  Status: %s, Passing Grade: %.1f%%%n",
                getStudentStatus(), PASSING_GRADE);
    }

    @Override
    public boolean isPassing(double currentAverage) {
        return currentAverage >= PASSING_GRADE;
    }
}