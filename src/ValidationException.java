public class ValidationException extends Exception {
    //this is the exception thrown when input validation fails

    public ValidationException(String message) {
        super("Validation failed: " + message);
    }
}
