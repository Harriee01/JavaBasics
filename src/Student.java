import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

//Implements Serializable for binary export, follows SOLID principles and designed for thread-safe operations.
public abstract class Student implements Serializable, Gradable {
    // Serializable marker for binary export
    private static final long serialVersionUID = 1L;

    // AtomicInteger is used for thread-safe ID generation
    private static final AtomicInteger studentCounter = new AtomicInteger(1000);

    // Final fields for immutability (Thread safety)
    private final String studentId;           // Immutable unique identifier
    private final String studentType;         // Immutable student type

    // Mutable fields with proper encapsulation
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private int studentAge;
    private String studentStatus;

    // No GradeManager dependency (Dependency Inversion Principle)
    // No search logic in entity (Single Responsibility Principle)
    // No export logic in entity (Interface Segregation Principle)

    //Constructor for Student which is initialized with thread-safe ID generation.
    protected Student(String studentName, String studentEmail,
                      String studentPhone, int studentAge, String studentType) {
        // Thread-safe ID generation using AtomicInteger
        this.studentId = String.format("STU%04d", studentCounter.incrementAndGet());
        this.studentType = studentType; // Set by subclass constructor

        // Initialize mutable fields
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.studentAge = studentAge;
        this.studentStatus = "ACTIVE"; // Default status
    }

    // Getter methods

    //Returns the student's unique ID and used as key in HashMap for O(1) lookup
    public String getStudentId() {
        return studentId;
    }

    //Returns the student type ("Regular" or "Honors")
    public String getStudentType() {
        return studentType;
    }

    //Returns the student's name.

    public String getStudentName() {
        return studentName;
    }

    //Returns the student's email address.
    public String getStudentEmail() {
        return studentEmail;
    }

    //Returns the student's phone number.
    public String getStudentPhone() {
        return studentPhone;
    }

    //Returns the student's age.
    public int getStudentAge() {
        return studentAge;
    }

    //Returns the student's current status.
    public String getStudentStatus() {
        return studentStatus;
    }


    //Updates the student's name.Validation is performed by StudentManager
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    //Updates the student's email.Email format validation  will be performed by StudentManager using regex.
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    //Updates the student's phone number.Phone format validation will be performed by StudentManager using regex.
    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    //Updates the student's age.Age validation should be performed by StudentManager.
    public void setStudentAge(int studentAge) {
        this.studentAge = studentAge;
    }

    //Updates the student's status.

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }



    //Gradable interface implementation
    //Validates if a grade is within acceptable range (0-100).
    @Override
    public boolean validateGrade(double grade) {
        return grade >= 0 && grade <= 100;
    }

    //Records a grade (placeholder - actual recording  is done by GradeManager) .Returns true if grade is valid.
    @Override
    public boolean recordGrade(double grade) {
        return validateGrade(grade);
    }





}