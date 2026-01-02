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


    // Abstract methods that will be implemented by subclasses

    //Returns the minimum passing grade for this student type
    public abstract double getPassingGrade();

    //Displays student details in a formatted way.
    public abstract void displayStudentDetails();

    //Checks if the student is passing based on their average grade.
    public abstract boolean isPassing(double currentAverage);

    // Equals and hashcode for collection optimization

    //Students are equal if they have the same studentId important for HashMap/HashSet operations
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId.equals(student.studentId);
    }

    //HashCode based on studentId for efficient HashMap operations, consistent with equals() method.
    @Override
    public int hashCode() {
        return studentId.hashCode();
    }

    // to string method for debugging

    //Returns string representation of student for debugging.
    @Override
    public String toString() {
        return String.format("Student{id=%s, name='%s', type=%s, status=%s}",
                studentId, studentName, studentType, studentStatus);
    }

    // static methods for ID management

    //Returns the next available student ID without incrementing counter for previewing what the next ID will be
    public static String getNextStudentId() {
        return String.format("STU%04d", studentCounter.get() + 1);
    }

    //Resets the student counter (for testing purposes only).
     //Package-private to prevent misuse.
    static void resetCounterForTesting() {
        studentCounter.set(1000);
    }
}