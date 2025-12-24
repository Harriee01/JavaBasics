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

    //Validates an email address format.
    //Throws ValidationException if email is invalid.
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty.");
        }

        // Use pre-compiled pattern for faster matching
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

     //Validates a phone number format.
     //Accepts international formats with country codes.
    public static void validatePhone(String phone) throws ValidationException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Phone number cannot be empty.");
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone number format: " + phone);
        }
    }

    //Validates a student ID format (STU followed by 3 digits).
    public static void validateStudentId(String studentId) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty.");
        }

        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new ValidationException("Student ID must be in format STU001, STU002, etc.");
        }
    }

    //Validates a student's name.
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty.");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ValidationException("Name must contain only letters and spaces (2-50 characters).");
        }
    }


     //Validates a grade value must be between 0 and 100
    public static void validateGrade(double grade) throws ValidationException {
        if (grade < 0 || grade > 100) {
            throw new ValidationException(
                    String.format("Grade must be between 0 and 100. Received: %.2f", grade)
            );
        }
    }

     //Validates an age must be between 5 and 100
    public static void validateAge(int age) throws ValidationException {
        if (age < 5 || age > 100) {
            throw new ValidationException(
                    String.format("Age must be between 5 and 100. Received: %d", age)
            );
        }
    }









}
