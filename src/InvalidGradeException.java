// this exception is thrown when grade value is invalid (not 0-100)
public class InvalidGradeException extends Exception {
    // this constructor takes the invalid grade value
    public InvalidGradeException(double grade) {
        // Create message showing what was entered and what's valid
        super("Grade must be between 0 and 100. You entered: " + grade);
    }
}
