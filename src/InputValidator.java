import java.util.regex.Pattern; // imports the Pattern class(a utility class) to let the system work with regular expressions
// to validate inputs from user
//Follows Single Responsibility Principle - only handles validation.

public class InputValidator {

    // Pre-compiled regex patterns for better performance
    // These patterns are compiled once and reused multiple times
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9. ()-]{10,25}$");

    private static final Pattern STUDENT_ID_PATTERN =
            Pattern.compile("^STU\\d{3}$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z ]{2,50}$");



}
