public class HonorsStudent extends Student {
    private static final String STUDENT_TYPE = "Honors";
    private static final double PASSING_GRADE = 60.0;
    private static final double HONORS_THRESHOLD = 85.0;

    private boolean honorsEligible;

    public HonorsStudent(String name, String email, String phone, int age) {
        super(name, email, phone, age, STUDENT_TYPE);
        this.honorsEligible = false;
    }

    @Override
    public double getPassingGrade() {
        return PASSING_GRADE;
    }

    @Override
    public void displayStudentDetails() {
        System.out.printf("Honors Student: %s (ID: %s)%n",
                getStudentName(), getStudentId());
        System.out.printf("  Email: %s, Phone: %s, Age: %d%n",
                getStudentEmail(), getStudentPhone(), getStudentAge());
        System.out.printf("  Status: %s, Passing Grade: %.1f%%%n",
                getStudentStatus(), PASSING_GRADE);
        System.out.printf("  Honors Eligible: %s%n",
                honorsEligible ? "YES" : "NO");
    }

    @Override
    public boolean isPassing(double currentAverage) {
        return currentAverage >= PASSING_GRADE;
    }

    public void checkHonorsEligibility(double currentAverage) {
        honorsEligible = currentAverage >= HONORS_THRESHOLD;
    }

    public boolean isHonorsEligible() {
        return honorsEligible;
    }
}