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


}